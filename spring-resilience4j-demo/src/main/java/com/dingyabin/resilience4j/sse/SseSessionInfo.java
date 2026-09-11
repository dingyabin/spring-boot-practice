package com.dingyabin.resilience4j.sse;

import lombok.Getter;
import lombok.Setter;

/**
 * SSE 在线会话信息
 *
 */
@Getter
@Setter
public class SseSessionInfo {

    /**
     * 客户端标识
     */
    private String clientId;

    /**
     * 会话创建时间(毫秒时间戳)
     */
    private long createTime;

    /**
     * 最近一次成功推送时间(毫秒时间戳)
     */
    private long lastActiveTime;


    public SseSessionInfo(String clientId, long createTime, long lastActiveTime) {
        this.clientId = clientId;
        this.createTime = createTime;
        this.lastActiveTime = lastActiveTime;
    }
}
