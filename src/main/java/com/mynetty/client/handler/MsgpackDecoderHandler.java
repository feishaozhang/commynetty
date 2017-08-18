package com.mynetty.client.handler;

import com.mynetty.commom.msgpack.model.ProtocolMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
* 消息业务处理Handler
*/
public class MsgpackDecoderHandler extends ChannelHandlerAdapter{
private Logger logger = Logger.getLogger(this.getClass());

    public MsgpackDecoderHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage)msg;//Convert Object to ByteBuf
        logger.info("Read msg: "+message.getBody().getMessage());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("链接异常，关闭ctx"+cause.getMessage());
        ctx.close();
    }
}