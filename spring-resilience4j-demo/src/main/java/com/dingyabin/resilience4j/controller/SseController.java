package com.dingyabin.resilience4j.controller;


import com.dingyabin.resilience4j.sse.SseSessionInfo;
import com.dingyabin.resilience4j.sse.SseSessionManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.List;

/**
 * SSE 消息推送接口
 */
@RestController
@RequestMapping("/sse")
public class SseController {

    @Resource
    private SseSessionManager sseSessionManager;

    /*
        浏览器通过 EventSource 连接该接口接收推送; clientId 为空时服务端自动生成, 同一 clientId 重复连接会替换旧连接
     */
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestParam(required = false) String clientId) {
        SseEmitter emitter = sseSessionManager.connect(clientId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .header("X-Accel-Buffering", "no")
                .body(emitter);
    }

    /**
     * 查询在线会话列表
     *
     * @return
     */
    @GetMapping("/sessions")
    public List<SseSessionInfo> sessions() {
        return sseSessionManager.listSessions();
    }

    /**
     * 向指定会话推送消息
     */
    @GetMapping("/send")
    public boolean send(@RequestParam String clientId,
                        @RequestParam(defaultValue = "message") String eventName,
                        @RequestParam String message) {
        return sseSessionManager.send(clientId, eventName, message);
    }

    /**
     * 向所有会话广播消息
     */
    @GetMapping("/broadcast")
    public int broadcast(@RequestParam(defaultValue = "message") String eventName,
                         @RequestParam String message) {
        return sseSessionManager.broadcast(eventName, message);
    }

    /**
     * 踢掉指定会话
     */
    @GetMapping("/disconnect")
    public boolean disconnect(@RequestParam String clientId) {
        return sseSessionManager.disconnect(clientId);
    }
}
