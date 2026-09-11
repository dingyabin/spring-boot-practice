package com.dingyabin.resilience4j.sse;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 单个 SSE 会话, 包装 SseEmitter
 */
@Slf4j
@Getter
public class SseSession {

    /**
     * 客户端标识
     */
    private final String clientId;

    /**
     * SSE 发射器
     */
    private final SseEmitter emitter;

    /**
     * 会话创建时间
     */
    private final long createTime;

    /**
     * 最近一次成功推送时间, 由心跳/探活持续刷新
     */
    private volatile long lastActiveTime;

    /**
     * 会话是否已关闭, 防止重复关闭
     */
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SseSession(String clientId, SseEmitter emitter) {
        this.clientId = clientId;
        this.emitter = emitter;
        this.createTime = System.currentTimeMillis();
        this.lastActiveTime = this.createTime;
    }

    /**
     * 推送命名事件, 浏览器通过 EventSource.addEventListener 监听
     */
    public synchronized void sendEvent(String eventName, Object data) throws IOException {
        checkClosed();
        emitter.send(SseEmitter.event().name(eventName).data(data));
        lastActiveTime = System.currentTimeMillis();
    }

    /**
     * 推送注释消息(SSE comment), 浏览器自动忽略, 用于心跳保活与探活
     */
    public synchronized void sendComment(String comment) throws IOException {
        checkClosed();
        emitter.send(SseEmitter.event().comment(comment));
        lastActiveTime = System.currentTimeMillis();
    }

    /**
     * 正常关闭会话
     */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            try {
                emitter.complete();
            } catch (Exception e) {
                log.debug("SSE 会话关闭异常, clientId={}, message={}", clientId, e.getMessage());
            }
        }
    }

    /**
     * 强制剔除会话(断线连接/僵尸连接)
     */
    public void kick(String reason) {
        if (closed.compareAndSet(false, true)) {
            try {
                emitter.completeWithError(new IOException(reason));
            } catch (Exception e) {
                log.debug("SSE 会话剔除异常, clientId={}, message={}", clientId, e.getMessage());
            }
            log.warn("SSE 会话已强制剔除, clientId={}, reason={}", clientId, reason);
        }
    }

    public SseSessionInfo toInfo() {
        return new SseSessionInfo(clientId, createTime, lastActiveTime);
    }

    private void checkClosed() throws IOException {
        if (closed.get()) {
            throw new IOException("会话已关闭, clientId=" + clientId);
        }
    }
}
