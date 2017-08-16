package com.mynetty.commom.msgpack;

import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

/**
 * MessagePack编码器
 * 高效，码流小，跨平台
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>{

    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {
        MessageBufferPacker msgpack = MessagePack.newDefaultBufferPacker();
        ProtocalMessage message = null;
        if(msg != null){
            message = (ProtocalMessage) msg;
            Header header = message.getHeader();
            Message messageBody = message.getBody();

            //编码
            msgpack.packInt(header.getCrcCode());//校验头
            msgpack.packInt(header.getLength());//消息长度
            msgpack.packLong(header.getSessionID());//sessionID
            msgpack.packByte(header.getType());//消息类型
            msgpack.packByte(header.getPriority());//消息优先级

            msgpack.packLong(messageBody.getFrom());
            msgpack.packLong(messageBody.getTarget());
            msgpack.packString(messageBody.getMessage());
            byteBuf.writeBytes(msgpack.toByteArray());
        }
    }
}
