package com.mynetty.commom.msgpack.messageEnum;

/**
 * 服务端返回给客户端的消息类型
 */
public enum MessageStatusEnum {

    /**
     * 认证成功
     */
    AUTH_SUCCESS((byte)1, "认证成功"),
    /**
     * 认证失败
     */
    AUTH_FAILD((byte)2, "认证失败"),
    /**
     *
     */
    REQUEST((byte)0, "用户请求状态");


    private byte code;
    private String description;

    MessageStatusEnum(byte code, String description){
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
