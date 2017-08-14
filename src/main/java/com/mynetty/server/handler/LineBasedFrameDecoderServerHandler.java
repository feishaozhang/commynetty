package com.mynetty.server.handler;

import com.mynetty.server.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;
/**
 * @version 1.0
 * LineBasedFrameDecoderServerHandler used to handle Data recept and transfer
 */
public class LineBasedFrameDecoderServerHandler extends ChannelHandlerAdapter{

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
            sendMessage(ctx,"server:Hi I'm server");
        }
    }

    /**
     * used to decode ByteBuf to String
     * @param msg ByteBuf
     * @see io.netty.buffer.ByteBuf
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

    public void sendMessage(ChannelHandlerContext ctx, String message){
        ByteBuf responseByteBuf = Unpooled.copiedBuffer(message.getBytes());
        ctx.write(responseByteBuf);
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