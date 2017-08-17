package com.mynetty.commom.msgpack.model;

/**
 * 消息通信协议
 */
public class Message {
    private long from;
    private long target;
    private String message;

    public Message() {
    }

    public Message(long from, long target, String message) {
        this.from = from;
        this.target = target;
        this.message = message;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


