package com.mynetty.client.handler;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
* 业务处理Handler
*/
public class MsgpackDecoderHandler extends ChannelHandlerAdapter{
private Logger logger = Logger.getLogger(this.getClass());

    public MsgpackDecoderHandler() {

    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        ProtocalMessage msg = MessageTool.getProtocolMessage("1000", 1000L, 0L, MessageTypeEnum.AUTH_CHANNEL_REQ, MessageStatusEnum.REQUEST,"1000");
        MessageSender.sendMessage(ctx, msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocalMessage message = (ProtocalMessage)msg;//Convert Object to ByteBuf

        logger.info("Read msg: "+message.getBody().getMessage());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downStream "+cause.getMessage());
        ctx.close();
    }
}