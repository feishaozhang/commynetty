package com.mynetty.server.handler;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.server.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @version 1.0
 * LineBasedFrameDecoderServerHandler used to handle Data recept and transfer
 */
public class DelimiterBasedFrameDecoderServerHandler extends ChannelHandlerAdapter{

    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * when Channel read data
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String realMessage = decodeMessage(msg);
        if(realMessage != null){
            realMessage += Configuration.DELIMITER_DECODER_TAG;
            ByteBuf responseByteBuf = Unpooled.copiedBuffer(realMessage.getBytes());
            MessageSender.sendMessage(ctx, responseByteBuf);
        }
    }

    /**
     * used to decode ByteBuf to String
     * @param msg ByteBuf
     * @see ByteBuf
     * @return String message
     */
    public String decodeMessage(Object msg){
        if(msg != null) {
            String message = (String) msg;
            logger.info("Server receive message: " + message);
            return message;
        }
        logger.info("Get a message");
        return null;
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