package com.mynetty.commom.msgpack.model;

import java.util.HashMap;
import java.util.Map;

public final class Header {
    /**
     * netty消息校验码三部分
     * 1、0xccdc 固定值，表明消息是系统的协议消息
     * 2、主版本号：1~255 1byte
     * 3、次版本号:1~255 1byte
     * crcCode = oxccdc+主版本号+次版本号
     */
    private int crcCode = 0xccdc0101;
    /**
     * 消息长度，包括消息头和消息体
     */
    private int length;//消息长度
    /**
     * 集群节点全局唯一,由会话ID生成器生成
     */
    private long sessionID;//会话ID
    /**
     * 消息类型
     * 0 业务请求消息
     * 1 业务相应消息
     * 2 握手请求消息
     * 3 握手应答消息
     * 4 心跳请求消息
     * 5 心跳应答消息
     */
    private byte type;//消息类型
    /**
     * 消息优先级0~255
     */
    private byte priority;//消息优先级
    /**
     * 拓展消息头
     */
    private Map<String, Object> attachment = new HashMap<String, Object>();//附件

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }
}
