package com.mynetty.commom.msgpack;

import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
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

        Header header = decodeHeader(unpacker);
        Message msg = getMessage(unpacker);

        ProtocalMessage pMessage = new ProtocalMessage(header, msg);

        list.add(pMessage);
    }

    public Header decodeHeader(MessageUnpacker unpacker)throws Exception{

        //Decode Header
        int crcCode = unpacker.unpackInt();
        int bodyLength = unpacker.unpackInt();
        Long sessionId = unpacker.unpackLong();
        byte type = unpacker.unpackByte();
        byte priority = unpacker.unpackByte();
        Header header = new Header(crcCode,bodyLength, sessionId, type, priority);
        return header;
    }

    public Message getMessage(MessageUnpacker unpacker)throws Exception{
        //Decode Message
        Long from = unpacker.unpackLong();
        Long target = unpacker.unpackLong();
        String realMessage = unpacker.unpackString();

        Message msg = new Message(from, target, realMessage);
        return msg;
    }
}
