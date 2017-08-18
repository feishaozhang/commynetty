package com.mynetty.commom.msgpack;

import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import com.mynetty.server.cache.SessionChannelCache;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.log4j.Logger;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.util.List;

/**
 * 使用MsgpackDecoder进行解码
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * 解码
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
          final byte[] array;
          final int length = byteBuf.readableBytes();
          array = new byte[length];
          byteBuf.getBytes(byteBuf.readerIndex(),array,0,length);
          //解码
          MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(array);
          Header header = decodeHeader(unpacker);
          Message msg = getMessage(unpacker);

          ProtocolMessage pMessage = new ProtocolMessage(header, msg);
          list.add(pMessage);
    }

    /**
     * 解码消息头
     * @param unpacker
     * @return
     * @throws Exception
     */
    public Header decodeHeader(MessageUnpacker unpacker)throws Exception{

        //Decode Header
        int crcCode = unpacker.unpackInt();
        int bodyLength = unpacker.unpackInt();
        Long sessionId = unpacker.unpackLong();
        byte type = unpacker.unpackByte();
        byte priority = unpacker.unpackByte();
        byte status = unpacker.unpackByte();
        String auth = unpacker.unpackString();
        Header header = new Header(crcCode, bodyLength, sessionId, type, priority, status, auth);
        return header;
    }

    /**
     * 解码消息体
     * @param unpacker
     * @return
     * @throws Exception
     */
    public Message getMessage(MessageUnpacker unpacker)throws Exception{
        //Decode Message
        Long from = unpacker.unpackLong();
        Long target = unpacker.unpackLong();
        String realMessage = unpacker.unpackString();

        Message msg = new Message(from, target, realMessage);
        return msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause);
        //客户端非正常退出时，需要手动关闭句柄
        SessionChannelCache.removeSession(ctx.channel().id().asLongText());
    }
}
