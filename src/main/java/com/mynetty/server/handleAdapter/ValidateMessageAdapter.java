package com.mynetty.server.handleAdapter;

import com.mynetty.commom.msgpack.model.Header;
import com.mynetty.commom.msgpack.model.ProtocolConfig;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * 验证消息头是否正确，如果不正确则拒绝
 */
public class ValidateMessageAdapter extends ChannelHandlerAdapter {
    private final Logger logger = Logger.getLogger(this.getClass());
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage) msg;
        if(!validateRequestAvaliable(message)){
            logger.info("链路认证失败，关闭链路!");
            ctx.close();
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 验证消息的有效性,当前只杨正请求头信息
     * @param message
     */
    public boolean validateRequestAvaliable(ProtocolMessage message){
        int crcCode = message.getHeader().getCrcCode();
        if(crcCode == ProtocolConfig.crcCode){
            return true;
        }
        return false;

    }

}
