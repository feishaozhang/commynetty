package com.mynetty.commom.msgpack.model;

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
     * @see com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum
     */
    private byte type;//消息类型
    /**
     * 消息优先级0~255
     */
    private byte priority;//消息优先级

    /**
     * 消息状态
     * @see
     */
    private byte status;

    /**
     * 验证位
     */
    private String auth;



    public Header() {
    }

    public Header(int crcCode, int length, long sessionID, byte type, byte priority, byte status,String auth) {
        this.crcCode = crcCode;
        this.length = length;
        this.sessionID = sessionID;
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.auth = auth;
    }

    public int getCrcCode() {
        return crcCode;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
