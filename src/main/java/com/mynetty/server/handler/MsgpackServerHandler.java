package com.mynetty.server.handler;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
import com.mynetty.commom.msgpack.model.ProtocolConfig;
import com.mynetty.server.cache.SessionChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * Msgpack 消息处理类
 * @version 1.0
 * LineBasedFrameDecoderServerHandler used to handle Data recept and transfer
 */
public class MsgpackServerHandler extends ChannelHandlerAdapter{

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * 链路激活回调函数
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocalMessage message = (ProtocalMessage)msg;//Convert Object to ByteBuf
        //校验消息有效性
        if(!validateRequestAvaliable(message)){
            logger.info("链路认证失败，关闭链路!");
            ctx.close();
            return ;
        }

        //消息处理
        if(processMessage(ctx,message)){
            sendMessage(ctx.channel(), message);
        }
    }

    /**
     * 处理读取的消息
     *
     * @param ctx
     * @param message
     * @return true 处理成功 false 处理失败
     */
    public boolean processMessage(ChannelHandlerContext ctx, ProtocalMessage message){
        logger.info("消息处理链=======================>");
        //消息头
        Header header = message.getHeader();
        //消息内容
        Message messageBody = message.getBody();
        //============================================开始处理消息
        byte type = message.getHeader().getType();
        MessageTypeEnum mType = MessageTypeEnum.getMessageType(type);
        switch (mType){
            case MESSAGE_BUSSINESS://消息路由,当用户在线则发送消息，否则返回发送者一条消息“当前用户已经下线”
                long target = messageBody.getTarget();
                Channel channel = SessionChannelCache.getSession(target);
                if(channel != null){

                    ProtocalMessage reSendPM = MessageTool.getProtocolMessage(messageBody.getMessage(),messageBody.getFrom(),messageBody.getTarget(),MessageTypeEnum.MESSAGE_BUSSINESS,MessageStatusEnum.REQUEST);
                    MessageSender.sendMessage(channel, reSendPM);
                    logger.info("消息已经发送"+"从用户 "+messageBody.getFrom() +"到 "+ messageBody.getTarget());
                }else{
                    ProtocalMessage reSendPM = MessageTool.getProtocolMessage("当前用户已经下线",0L,messageBody.getFrom(),MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
                    MessageSender.sendMessage(channel, reSendPM);
                    logger.info("当前用户已经下线");
                }
                break;
            case OFF_LINE://用户下线

                break;
        }
        return false;
    }



    /**
     * 验证消息的有效性,当前只杨正请求头信息
     * @param message
     */
    public boolean validateRequestAvaliable(ProtocalMessage message){
        int crcCode = message.getHeader().getCrcCode();
        if(crcCode == ProtocolConfig.crcCode){
            return true;
        }
        return false;

    }

    public void sendMessage(Channel channel, Object message){
        channel.writeAndFlush(message);
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