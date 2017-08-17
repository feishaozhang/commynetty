package com.mynetty.client.handler;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
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
public class FixedLengthFrameDecoderHandler extends ChannelHandlerAdapter{
private Logger logger = Logger.getLogger(this.getClass());
    private ByteBuf firstMessage;

    public FixedLengthFrameDecoderHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       for (int i=0;i<100;i++){
           byte[] req =("Hello I'm message which from Client" + System.getProperty("line.separator")).getBytes();
           firstMessage = Unpooled.buffer(req.length);
           firstMessage.writeBytes(req);
           MessageSender.sendMessage(ctx, firstMessage);
           logger.info("sending message : "+i);
       }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String)msg;//Convert Object to ByteBuf
        logger.info("Read msg: "+message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downStream "+cause.getMessage());
        ctx.close();
    }
}