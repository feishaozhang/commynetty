package com.mynetty.client.channeInitializer;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.client.handler.DelimiterBasedFrameDecoderHandler;
import com.mynetty.client.handler.MsgpackDecoderHandler;
import com.mynetty.client.handler.heartbeat.HeartBeatReqHandler;
import com.mynetty.commom.msgpack.MsgpackDecoder;
import com.mynetty.commom.msgpack.MsgpackEncoder;
import com.mynetty.server.handler.MsgpackServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import sun.plugin2.message.HeartbeatMessage;

public class MsgpackChannelInitializer extends ChannelInitializer {

    protected void initChannel(Channel socketChannel) throws Exception {
        //用于处理半包
        socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));

        socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
        socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
        socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
        socketChannel.pipeline().addLast("HeartBeatReqHandler",new HeartBeatReqHandler());
        socketChannel.pipeline().addLast(new MsgpackDecoderHandler());
    }
}
