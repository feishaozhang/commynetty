package com.mynetty.commom.msgpack.encoderTool;

import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;

/**
 * 生成一个消息
 *
 */
public class MessageTool {

    /**
     * 生成一个完整的消息包
     * @param msg
     * @param from
     * @param target
     * @param messageType
     * @param messateStatus
     * @return
     */
    public static ProtocalMessage getProtocolMessage(String msg, long from, long target
            , MessageTypeEnum messageType, MessageStatusEnum messateStatus, String auth){
        ProtocalMessage message = new ProtocalMessage(getHeader(messageType, messateStatus, auth), getMessage(msg, from, target));
        return message;
    }

    /**
     * 生成一个消息包，不需要验证串，一般服务器使用用于返回数据
     * @param msg
     * @param from
     * @param target
     * @param messageType
     * @param messateStatus
     * @return
     */
    public static ProtocalMessage getProtocolMessage(String msg, long from, long target
            , MessageTypeEnum messageType, MessageStatusEnum messateStatus){
        ProtocalMessage message = new ProtocalMessage(getHeader(messageType, messateStatus, ""), getMessage(msg, from, target));
        return message;
    }

    /**
     * 生成只有消息头的消息包
     * @param messageType
     * @param messateStatus
     * @return
     */
    public static ProtocalMessage getProtocolMessage(MessageTypeEnum messageType, MessageStatusEnum messateStatus, String auth){
        ProtocalMessage message = new ProtocalMessage(getHeader(messageType,messateStatus,auth), new Message());
        return message;
    }

    public static Message getMessage(String msg, long from, long target){
        Message message = new Message();
        message.setFrom(from);
        message.setTarget(target);
        message.setMessage(msg);
        return message;
    }

    public static ProtocalMessage getProtocolMessage(MessageTypeEnum messageType, MessageStatusEnum messateStatus){
        return getProtocolMessage(messageType, messateStatus, "");
    }

    public static Header getHeader(MessageTypeEnum messageType, MessageStatusEnum messateStatus, String auth){
        Header header = new Header();
        header.setType(messageType.getMessageCode());
        header.setLength(0);
        header.setPriority((byte)0);
        header.setSessionID(0);
        header.setAuth(auth);
        header.setStatus(messateStatus.getCode());
        return header;
    }
}
