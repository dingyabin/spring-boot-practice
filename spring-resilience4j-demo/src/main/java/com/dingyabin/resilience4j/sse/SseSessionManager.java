package com.dingyabin.resilience4j.sse;

import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE 会话管理器
 * <p>
 * 负责会话的注册、移除、消息推送、心跳保活以及"僵尸"连接清理
 */
@Slf4j
@Component
public class SseSessionManager {

    /**
     * 在线会话集合, key 为 clientId
     */
    private final Map<String, SseSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * 建立 SSE 连接并注册会话
     *
     * @param clientId 客户端标识, 为空时自动生成 UUID
     * @return SseEmitter 交给 Spring MVC 接管, 浏览器通过 EventSource 接收推送
     */
    public SseEmitter connect(String clientId) {
        String id = StringUtils.isBlank(clientId) ? UUID.randomUUID().toString() : clientId;
        SseEmitter emitter = new SseEmitter(0L);
        SseSession session = new SseSession(id, emitter);
        emitter.onCompletion(() -> removeSession(session, "连接已关闭"));
        emitter.onTimeout(() -> removeSession(session, "连接已超时"));
        emitter.onError(e -> removeSession(session, "连接发生异常: " + e.getMessage()));

        SseSession replaced = sessionMap.put(id, session);
        if (replaced != null) {
            replaced.kick("同一 clientId 建立了新连接, 旧连接被替换");
        }
        try {
            Map<String, Object> eventData = MapUtil.of("clientId", id);
            eventData.put("timestamp", System.currentTimeMillis());
            session.sendEvent("connected", eventData);
        } catch (Exception e) {
            removeSession(session, true, "连接建立后首次推送失败: " + e.getMessage());
            return emitter;
        }
        log.info("SSE 会话注册成功, clientId={}, 当前在线会话数={}", id, sessionMap.size());
        return emitter;
    }

    /**
     * 主动断开并移除指定会话
     *
     * @return 会话是否存在并被移除
     */
    public boolean disconnect(String clientId) {
        SseSession session = sessionMap.remove(clientId);
        if (session == null) {
            return false;
        }
        session.close();
        log.info("SSE 会话已关闭, clientId={}, 当前在线会话数={}", clientId, sessionMap.size());
        return true;
    }

    /**
     * 向指定会话推送事件, 推送失败视为断线并剔除
     *
     * @return 是否推送成功
     */
    public boolean send(String clientId, String eventName, Object data) {
        SseSession session = sessionMap.get(clientId);
        if (session == null) {
            log.warn("SSE 消息推送失败, 会话不存在, clientId={}, event={}", clientId, eventName);
            return false;
        }
        try {
            session.sendEvent(eventName, data);
            return true;
        } catch (Exception e) {
            removeSession(session, true, "消息推送失败, 判定为断线连接: " + e.getMessage());
            return false;
        }
    }

    /**
     * 向所有在线会话广播事件
     *
     * @return 推送成功的会话数
     */
    public int broadcast(String eventName, Object data) {
        int successCount = 0;
        for (SseSession session : sessionMap.values()) {
            try {
                session.sendEvent(eventName, data);
                successCount++;
            } catch (Exception e) {
                removeSession(session, true, "广播推送失败, 判定为断线连接: " + e.getMessage());
            }
        }
        log.info("SSE 广播完成, event={}, 成功会话数={}", eventName, successCount);
        return successCount;
    }


    /**
     * 查询所有在线会话信息
     */
    public List<SseSessionInfo> listSessions() {
        List<SseSessionInfo> result = new ArrayList<>(sessionMap.size());
        sessionMap.values().forEach(session -> result.add(session.toInfo()));
        return result;
    }



    private void removeSession(SseSession session, String reason) {
        removeSession(session, false, reason);
    }


    private void removeSession(SseSession session, boolean kickOut, String reason) {
        if (sessionMap.remove(session.getClientId()) != null) {
            log.info("SSE 会话移除, clientId={}, reason={}, 当前在线会话数={}", session.getClientId(), reason, sessionMap.size());
        }
        if (kickOut) {
            session.kick(reason);
        }
    }

    /**
     * 应用关闭时统一释放所有会话
     */
    @PreDestroy
    public void destroy() {
        sessionMap.values().forEach(SseSession::close);
        sessionMap.clear();
        log.info("SSE 会话管理器已销毁, 所有会话已关闭");
    }



    /**
     * 心跳保活: 定时向所有会话发送注释消息， 发送失败说明连接已不可用, 直接剔除
     * <p>
     * 间隔需小于 Nginx proxy_read_timeout(默认60秒), 否则长连接会被代理掐断
     */
    @Scheduled(fixedDelayString = "${sse.heartbeat-interval-millis:15000}")
    public void heartbeat() {
        if (sessionMap.isEmpty()) {
            return;
        }
        int kickedCount = 0;
        for (SseSession session : sessionMap.values()) {
            try {
                session.sendComment("heartbeat " + System.currentTimeMillis());
            } catch (Exception e) {
                kickedCount++;
                removeSession(session, true, "心跳发送失败, 判定为断线连接: " + e.getMessage());
            }
        }
        log.debug("SSE 心跳保活完成, 在线会话数={}, 剔除断线连接数={}", sessionMap.size(), kickedCount);
    }
}
