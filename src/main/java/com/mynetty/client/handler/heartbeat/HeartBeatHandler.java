package com.mynetty.client.handler.heartbeat;

import com.mynetty.commom.msgpack.model.Message;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatHandler extends ChannelHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;
        //返回心跳应答消息
        if (message != null) {
            Message heartBeatMsg = buildHeartBeat();
            ctx.writeAndFlush(heartBeatMsg);

        }
        else{
            ctx.fireChannelRead(msg);
        }
    }

    private Message buildHeartBeat(){
        Message msg = new Message();
        return msg;
    }
}
