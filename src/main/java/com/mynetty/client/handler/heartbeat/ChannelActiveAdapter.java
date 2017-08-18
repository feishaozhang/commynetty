package com.mynetty.client.handler.heartbeat;

import com.mynetty.client.cache.ClientCache;
import com.mynetty.commom.msgpack.encoderTool.EncryptTool;
import com.mynetty.commom.msgpack.encoderTool.MessageBuilder;
import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * 客户端心跳Handler
 */
public class ChannelActiveAdapter extends ChannelHandlerAdapter{
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("链路激活");
        ClientCache.addCacheValue("channelContext",ctx);
        //链路激活发送验证消息到服务器进行验证，auth为验证码
        String userId = ClientCache.getCacheValue("userId", String.class);
        long userIdLong = Long.parseLong(userId);
        //加密密钥
        String auth = EncryptTool.encrype(userId);
        if( (userId != null) && (auth != null)){
            MessageBuilder msgBuilder = new MessageBuilder();
            msgBuilder.setHeaderAuth(auth)
                    .setHeaderStatus(MessageStatusEnum.REQUEST)
                    .setHeaderType(MessageTypeEnum.AUTH_CHANNEL_REQ)
                    .setMessageDetail("")
                    .setMessageFrom(userIdLong)
                    .setMessageTarget(0);
            MessageSender.sendMessage(ctx, msgBuilder.build());
        }
        else{
            throw new Exception("请在客户端设置userID后再进行验证!");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       logger.error(cause);
    }
}
