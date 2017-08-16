package com.mynetty.commom.msgpack.model;

/**
 * 消息通信协议
 */
public class Message {
    private Long from;
    private Long target;
    private String message;

    public Message() {
    }

    public Message(Long from, Long target, String message) {
        this.from = from;
        this.target = target;
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


