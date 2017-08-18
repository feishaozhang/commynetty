package com.mynetty.commom.msgpack.encoderTool;

import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocolMessage;

/**
 * MessageBuilder
 * 消息建造类
 */
public class MessageBuilder {

    private Message messageBody;
    private Header messageHeader;

    public MessageBuilder() {
        messageBody = new Message();
        messageHeader = new Header();
    }

    public MessageBuilder setMessageDetail(String message){
        this.messageBody.setMessage(message);
        return this;
    }

    public MessageBuilder setMessageFrom(long from){
        this.messageBody.setFrom(from);
        return this;
    }

    public MessageBuilder setMessageTarget(long target){
        this.messageBody.setFrom(target);
        return this;
    }

    public MessageBuilder setHeaderType(MessageTypeEnum type){
        this.messageHeader.setType(type.getMessageCode());
        return this;
    }

    public MessageBuilder setHeaderAuth(String auth){
        this.messageHeader.setAuth(auth);
        return this;
    }

    public MessageBuilder setHeaderStatus(MessageStatusEnum status){
        this.messageHeader.setStatus(status.getCode());
        return this;
    }

    public MessageBuilder setCrcCode(int crcCode){
        this.messageHeader.setCrcCode(crcCode);
        return this;
    }

    public ProtocolMessage build(){
       return new ProtocolMessage(messageHeader,messageBody);
    }
}
