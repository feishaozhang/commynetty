package com.mynetty.server.handleAdapter;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * 服务端心跳回覆
 */
public class HeartBeatRespHandlerAdapter extends ChannelHandlerAdapter {
    private final Logger logger = Logger.getLogger(this.getClass());
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage) msg;
        Header header = message.getHeader();

        if((header != null) && (header.getType() == MessageTypeEnum.HEART_BEAT_REQ.getMessageCode())){
            logger.debug("收到客户端的心跳请求");
            ProtocolMessage pm =  MessageTool.getProtocolMessage(MessageTypeEnum.HEART_BEAT_RES, MessageStatusEnum.REQUEST);
            MessageSender.sendMessage(ctx, pm);
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }
}
