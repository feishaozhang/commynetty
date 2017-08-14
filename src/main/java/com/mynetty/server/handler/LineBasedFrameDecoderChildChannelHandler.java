package com.mynetty.server.handler;

import com.mynetty.server.Configuration;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class LineBasedFrameDecoderChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(Configuration.LINE_BASE_FRAME_DECODER_SIZE));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new LineBasedFrameDecoderServerHandler());
        }
}