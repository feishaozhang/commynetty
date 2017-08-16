package com.mynetty.client.coderTool;

import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;

public class MessageTool {

    public static ProtocalMessage getProtocolMessage(String msg, Long from, Long target, MessageTypeEnum messageType){
        ProtocalMessage message = new ProtocalMessage(getHeader(messageType), getMessage(msg, from, target));
        return message;
    }

    public static Message getMessage(String msg, Long from, Long target){
        Message message = new Message();
        message.setFrom(from);
        message.setTarget(target);
        message.setMessage(msg);
        return message;
    }

    public static Header getHeader(MessageTypeEnum messageType){
        Header header = new Header();
        header.setType(messageType.getMessageCode());
        header.setLength(0);
        header.setPriority((byte)0);
        header.setSessionID(0L);
        return header;
    }
}
