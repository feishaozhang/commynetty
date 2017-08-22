package com.mynetty.server.channelHandler;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.commom.msgpack.MsgpackDecoder;
import com.mynetty.commom.msgpack.MsgpackEncoder;
import com.mynetty.server.Configuration;
import com.mynetty.server.handleAdapter.AuthLoginChannelAdapter;
import com.mynetty.server.handleAdapter.CloseChannelAdapter;
import com.mynetty.server.handleAdapter.HeartBeatRespHandlerAdapter;
import com.mynetty.server.handleAdapter.ValidateMessageAdapter;
import com.mynetty.server.handler.DelimiterBasedFrameDecoderServerHandler;
import com.mynetty.server.handler.MsgpackServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 服务端消息链
 */
public class MsgpackChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
        //用于处理半包
        socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        socketChannel.pipeline().addLast(new MsgpackDecoder());
        socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
        socketChannel.pipeline().addLast(new MsgpackEncoder());
        socketChannel.pipeline().addLast("messageValidate", new ValidateMessageAdapter());//消息头验证链
        socketChannel.pipeline().addLast("authLoginChannelAdapter", new AuthLoginChannelAdapter());//登录验证链
        socketChannel.pipeline().addLast("readTimeOutHandler", new ReadTimeoutHandler(ClientConfiguration.READ_TIME_OUT));//60秒未收到任何消息判定为掉线
        socketChannel.pipeline().addLast("heartBeatResp", new HeartBeatRespHandlerAdapter());//心跳链
        socketChannel.pipeline().addLast("closeChannelAdapter", new CloseChannelAdapter());//关闭链路时释放资源
        socketChannel.pipeline().addLast(new MsgpackServerHandler());
        }
        }