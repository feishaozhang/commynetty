package com.mynetty.commom.msgpack.model;

/**
 * 传输消息协议
 */
public class ProtocolMessage {
    private Header header;//消息头
    private Message body;//消息体

    public ProtocolMessage() {
    }

    public ProtocolMessage(Header header, Message body) {
        this.header = header;
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Message getBody() {
        return body;
    }

    public void setBody(Message body) {
        this.body = body;
    }
}
