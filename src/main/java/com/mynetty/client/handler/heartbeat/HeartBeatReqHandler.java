package com.mynetty.client.handler.heartbeat;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 客户端心跳Handler
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter{
    private volatile ScheduledFuture<?> heartBeat;
    private Logger logger = Logger.getLogger(this.getClass());
    private final Object heartBeatLocker = new Object();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage)msg;
        //返回心跳应答消息
        Header header = message.getHeader();
        //服务器发回的验证成功
        if (   (header != null)
            && (header.getType()== MessageTypeEnum.AUTH_CHANNEL_RES.getMessageCode())
            && (header.getStatus() == MessageStatusEnum.AUTH_SUCCESS.getCode())){

            if(heartBeat == null) {//避免开启两次的心跳定时器
                synchronized (heartBeatLocker) {
                    if (heartBeat == null) {
                        heartBeat = ctx.executor().scheduleAtFixedRate(
                                new HeartBeatReqHandler.HeartBeatTask(ctx)
                                , ClientConfiguration.HEART_BEAT_DELAY, ClientConfiguration.HEART_BEAT_INTERVAL, TimeUnit.MILLISECONDS);
                    }
                }
            }
            logger.debug("认证成功启动心跳定时器");
        }
        else if((header != null) && (header.getType() == MessageTypeEnum.HEART_BEAT_RES.getMessageCode())){
            logger.debug("收到服务端的心跳回复");
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 心跳TASK
     */
    public class HeartBeatTask implements Runnable{
        private Logger logger = Logger.getLogger(this.getClass());
        private ChannelHandlerContext ctx;

        HeartBeatTask(final ChannelHandlerContext ctx){
            this.ctx = ctx;
        }

        public void run() {
            ProtocolMessage msg = MessageTool.getProtocolMessage(MessageTypeEnum.HEART_BEAT_REQ, MessageStatusEnum.REQUEST);
            MessageSender.sendMessage(ctx, msg);
            logger.debug("客户端发起心跳请求");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
