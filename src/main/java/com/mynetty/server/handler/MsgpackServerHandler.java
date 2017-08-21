package com.mynetty.server.handler;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import com.mynetty.server.cache.SessionChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * Msgpack 消息处理类
 * @version 1.0
 */
public class MsgpackServerHandler extends ChannelHandlerAdapter{
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * 消息业务处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage)msg;
        //消息处理
        if(processMessage(ctx,message)){
            MessageSender.sendMessage(ctx.channel(), message);
        }
        //=================消息最终消费者，不再向后传递========================
    }

    /**
     * 处理读取的消息
     *
     * @param ctx
     * @param message
     * @return true 处理成功 false 处理失败
     */
    public boolean processMessage(ChannelHandlerContext ctx, ProtocolMessage message){
        //消息内容
        Message messageBody = message.getBody();

        //开始处理消息
        byte type = message.getHeader().getType();
        MessageTypeEnum mType = MessageTypeEnum.getMessageType(type);
        switch (mType){

            case MESSAGE_BUSSINESS://消息转发
                messageBussiness(ctx, messageBody);
                break;

            case OFF_LINE://用户下线
                String channelId = ctx.channel().id().asLongText();
                kickUser(channelId);
                break;
        }
        return false;
    }


    /**
     * 消息转发业务
     * 消息路由,当用户在线则发送消息，否则返回发送者一条消息“当前用户已经下线”
     * @param messageBody 消息体
     */
    public void messageBussiness(ChannelHandlerContext ctx, Message messageBody){
        long target = messageBody.getTarget();
        Channel channel = SessionChannelCache.getSession(target);
        if(channel != null){
            ProtocolMessage reSendPM = MessageTool.getProtocolMessage(messageBody.getMessage(),messageBody.getFrom(),messageBody.getTarget(),MessageTypeEnum.MESSAGE_BUSSINESS,MessageStatusEnum.REQUEST);
            MessageSender.sendMessage(channel, reSendPM);
            logger.debug("消息已经发送"+"从用户 "+messageBody.getFrom() +"到 "+ messageBody.getTarget());
        }else{
            ProtocolMessage reSendPM = MessageTool.getProtocolMessage("当前用户已经下线",0L,messageBody.getFrom(),MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
            MessageSender.sendMessage(ctx.channel(), reSendPM);
            logger.debug("当前用户已经下线");
        }
    }


    /**
     * 下线
     * @param
     */
    public void kickUser(String channelId ){
        SessionChannelCache.removeSession(channelId);
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
       logger.info("Error occur "+cause.getMessage());

    }

}