package com.mynetty.client.handler;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.commom.msgpack.model.Message;
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
       Message msg = new Message();
       msg.setFrom(100L);
       msg.setTarget(200L);
       msg.setMessage("I'm from Client");
       ctx.write(msg);
       try{
           Thread.sleep(1000);
       }catch (Exception e){

       }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;//Convert Object to ByteBuf
        logger.info("Read msg: "+message.getMessage());
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