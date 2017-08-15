package com.mynetty.commom.msgpack;

import com.mynetty.commom.msgpack.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.util.List;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(array);
        Long from = unpacker.unpackLong();
        Long target = unpacker.unpackLong();
        String realMessage = unpacker.unpackString();

        list.add(getMessage(from, target, realMessage));
    }

    public Message getMessage(Long from, Long target, String message){
        Message msg = new Message();
        msg.setFrom(from);
        msg.setTarget(target);
        msg.setMessage(message);
        return msg;
    }
}
