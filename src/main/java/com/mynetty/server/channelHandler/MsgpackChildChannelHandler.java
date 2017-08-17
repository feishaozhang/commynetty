package com.mynetty.server.channelHandler;

import com.mynetty.commom.msgpack.MsgpackDecoder;
import com.mynetty.commom.msgpack.MsgpackEncoder;
import com.mynetty.server.Configuration;
import com.mynetty.server.handleAdapter.AuthLoginChannelAdapter;
import com.mynetty.server.handleAdapter.HeartBeatRespHandlerAdapter;
import com.mynetty.server.handler.DelimiterBasedFrameDecoderServerHandler;
import com.mynetty.server.handler.MsgpackServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class MsgpackChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {

            //用于处理半包
            socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
            socketChannel.pipeline().addLast(new MsgpackDecoder());
            socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
            socketChannel.pipeline().addLast(new MsgpackEncoder());
            socketChannel.pipeline().addLast("authLoginChannelAdapter", new AuthLoginChannelAdapter());//登录链
            socketChannel.pipeline().addLast("heartBeatResp", new HeartBeatRespHandlerAdapter());//心跳链
            socketChannel.pipeline().addLast(new MsgpackServerHandler());
        }
}