package com.mynetty.client.listener;

public interface ClientCallback {
    public void onComplete(OpType opType,String message);

    enum OpType{
        CONNECT_SUCCESS(0x01,"连接成功"),
        CONNECT_FAIL(0x02,"连接失败"),
        SEND_MESSAGE_SUCCESS(0x03,"发送消息成功"),
        SEND_MESSAGE_FAIL(0x04,"发送消息失败");

        int type;
        String description;

        private OpType(int type, String description){
            this.type = type;
            this.description = description;
        }


    }
}
