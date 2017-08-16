package com.mynetty.client.handler;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.client.coderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
     * LineBasedFrameDecoderHandler used to handle Readable message From channel
     * * We focus on 3 methods(channelActive, channelRead, exeptionCaught)
     * * When Client&Server finish 3 steps of TCP connection Netty will invoke channelActive()
     * * when server send message back to the Client method channelRead() will be invoked
     * *
     */
public class MsgpackDecoderHandler extends ChannelHandlerAdapter{
private Logger logger = Logger.getLogger(this.getClass());
    private ByteBuf firstMessage;

    public MsgpackDecoderHandler() {

    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        ProtocalMessage msg = MessageTool.getProtocolMessage("100000", 0L, 0L, MessageTypeEnum.AUTH_CHANNEL);
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocalMessage message = (ProtocalMessage)msg;//Convert Object to ByteBuf

        logger.info("Read msg: "+message.getBody().getMessage());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downStream "+cause.getMessage());
        ctx.close();
    }
}