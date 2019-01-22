package com.mynetty.client.channeInitializer;

import com.mynetty.client.handler.MsgpackDecoderHandler;
import com.mynetty.client.handler.heartbeat.ChannelActiveAdapter;
import com.mynetty.client.handler.heartbeat.HeartBeatReqHandler;
import com.mynetty.commom.msgpack.MsgpackDecoder;
import com.mynetty.commom.msgpack.MsgpackEncoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 客户端消息处理链
 */
public class MsgpackChannelInitializer extends ChannelInitializer<Channel> {

    protected void initChannel(Channel socketChannel) throws Exception {
        //用于处理半包
        socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
        socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
        socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());

        //链路激活handler
        socketChannel.pipeline().addLast("channelActiveHandler",new ChannelActiveAdapter());
        //心跳handler
        socketChannel.pipeline().addLast("HeartBeatReqHandler",new HeartBeatReqHandler());
        //消息Handler
        socketChannel.pipeline().addLast("messageBusinessHandler",new MsgpackDecoderHandler());
    }
}
