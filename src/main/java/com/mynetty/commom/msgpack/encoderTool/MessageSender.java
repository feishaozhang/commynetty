package com.mynetty.commom.msgpack.encoderTool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Netty消息发送器
 */
public class MessageSender {

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
        channel.writeAndFlush(msg);
    }
}
