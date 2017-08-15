package com.mynetty.commom.msgpack;

import com.mynetty.commom.msgpack.model.Message;
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

    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        MessageBufferPacker msgpack = MessagePack.newDefaultBufferPacker();
        Message message = null;
        if(msg != null){
            message = (Message) msg;
            msgpack.packLong(message.getFrom());
            msgpack.packLong(message.getTarget());
            msgpack.packString(message.getMessage());
            byteBuf.writeBytes(msgpack.toByteArray());
        }
    }
}
