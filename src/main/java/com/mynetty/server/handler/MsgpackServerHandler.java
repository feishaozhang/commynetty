package com.mynetty.server.handler;

import com.mynetty.commom.msgpack.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @version 1.0
 * LineBasedFrameDecoderServerHandler used to handle Data recept and transfer
 */
public class MsgpackServerHandler extends ChannelHandlerAdapter{

    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * when Channel read data
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;//Convert Object to ByteBuf
        logger.info("Read msg: "+message.getMessage());
        sendMessage(ctx, message);
    }

    public void sendMessage(ChannelHandlerContext ctx, Object message){
        ctx.write(message);
        logger.info("Server is writing message to log file");
    }

    /**
     * invoke when channel read data completely
     * then the channel will flush the data
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("ChannelHandlerContext error occur!"+cause.getMessage());
    }
}