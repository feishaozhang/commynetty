package com.mynetty.server.handleAdapter;

import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import com.mynetty.server.cache.SessionChannelCache;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * 登录验证
 */
public class AuthLoginChannelAdapter extends ChannelHandlerAdapter {
    private Logger logger = Logger.getLogger(this.getClass());


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage)msg;
        //返回心跳应答消息
        Header header = message.getHeader();
        //验证串
        String auth = header.getAuth();
        //客户端发起验证
        if ( (header != null) && ( header.getType() == MessageTypeEnum.AUTH_CHANNEL_REQ.getMessageCode() )){
            if(authUserConnection(auth)){ //用户验证携带用户参数进行验证
                long userId = Long.parseLong(header.getAuth());
                SessionChannelCache.addSession(userId ,ctx.channel());
                ProtocolMessage newMsg = MessageTool.getProtocolMessage("0",0L,0L,MessageTypeEnum.AUTH_CHANNEL_RES, MessageStatusEnum.AUTH_SUCCESS);
                MessageSender.sendMessage(ctx.channel(), newMsg);
            }
            else{
                //验证失败断开链路，不继续等待。
                ProtocolMessage newMsg = MessageTool.getProtocolMessage("0",0L,0L,MessageTypeEnum.AUTH_CHANNEL_RES, MessageStatusEnum.AUTH_FAILD);
                MessageSender.sendMessage(ctx.channel(), newMsg);
                ctx.close();
                logger.info("==>用户验证失败,关闭链路！");
            }
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 用户接入验证
     * @return
     */
    public boolean authUserConnection(String authCode){
        return true;
    }


}
