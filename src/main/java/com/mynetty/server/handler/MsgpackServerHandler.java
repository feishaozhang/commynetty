package com.mynetty.server.handler;

import com.mynetty.client.coderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
import com.mynetty.commom.msgpack.model.ProtocolConfig;
import com.mynetty.server.cache.SessionChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
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
            sendMessage(ctx, message);
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
        Message messageBody = message.getBody();
        Header header = message.getHeader();
        //开始处理消息
        byte type = message.getHeader().getType();
        MessageTypeEnum mType = MessageTypeEnum.getMessageType(type);
        switch (mType){
            case AUTH_CHANNEL://用户验证
                if(authUserConnection(messageBody)){ //用户验证携带用户参数进行验证
                    int userId = Integer.parseInt(message.getBody().getMessage());
                    SessionChannelCache.addSession(userId ,ctx.channel());
                    ProtocalMessage newMsg = MessageTool.getProtocolMessage("用户登录成功",0L,0L,MessageTypeEnum.HEART_BEAT_RES);
                    sendMessage(ctx, newMsg);
                    logger.info("==>用户验证成功,缓存Session！");
                }
                else{
                    ctx.close();
                    logger.info("==>用户验证失败,关闭链路！");
                }
                break;

            case HEART_BEAT_REQ://心跳请求消息
                //心跳
                break;

            case HEART_BEAT_RES://心跳回复消息
                break;

            case MESSAGE_BUSSINESS://消息路由

                logger.info("Read msg: "+message.getBody().getMessage());

                break;
            case OFF_LINE://用户下线

                break;
        }
        return false;
    }

    /**
     * 用户接入验证
     * @return
     */
    public boolean authUserConnection(Message message){
        return true;
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