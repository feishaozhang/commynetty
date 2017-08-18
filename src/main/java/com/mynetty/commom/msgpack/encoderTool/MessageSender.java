package com.mynetty.commom.msgpack.encoderTool;

import com.mynetty.commom.msgpack.model.ProtocolMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * Netty消息发送器
 */
public class MessageSender {
    private static Logger logger = Logger.getLogger(MessageSender.class);

    /**
     * 全局通用发送方法，用于后期统一处理相同操作
     * @param ctx ChannelHandlerContext
     * @param msg
     */
    public static void sendMessage(ChannelHandlerContext ctx, Object msg){
        sendMessage(ctx.channel(), msg);
    }

    /**
     * 全局通用发送方法，用于后期统一处理相同操作
     * @param channel Channel
     * @param msg
     */
    public static void sendMessage(Channel channel, Object msg){
     try{
         if(msg instanceof ProtocolMessage){
             channel.writeAndFlush(msg);
         }
         else{
             logger.error("消息类型不是ProtocolMessage");
         }
     }catch ( Exception e){
         e.printStackTrace();
     }

    }
}
