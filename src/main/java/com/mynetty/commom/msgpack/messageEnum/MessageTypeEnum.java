package com.mynetty.commom.msgpack.messageEnum;

public enum  MessageTypeEnum {
    /**
     * 消息类型
     * 2 心跳请求
     * 3 心跳响应
     * 4 消息业务
     * 5 用户登录认证
     * 6 下线
     */
    HEART_BEAT_REQ((byte)2,"心跳请求"),
    HEART_BEAT_RES((byte)3,"心跳答复"),
    MESSAGE_BUSSINESS((byte)4,"消息业务"),
    AUTH_CHANNEL((byte)5,"用户登录认证"),
    OFF_LINE((byte)6,"用户下线");

    private byte messageCode;
    private String messageDesc;


    MessageTypeEnum(byte code, String description){
        this.messageCode = code;
        this.messageDesc =description;
    }


    public byte getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(byte messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageDesc() {
        return messageDesc;
    }

    public void setMessageDesc(String messageDesc) {
        this.messageDesc = messageDesc;
    }

    public static MessageTypeEnum getMessageType(byte code){
        for (MessageTypeEnum type:values()) {
            if(type.getMessageCode() == code){
                return type;
            }
        }
        return null;
    }
}
