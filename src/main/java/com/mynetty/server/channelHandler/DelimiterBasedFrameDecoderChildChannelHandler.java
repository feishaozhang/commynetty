package com.mynetty.server.channelHandler;

import com.mynetty.server.Configuration;
import com.mynetty.server.handler.DelimiterBasedFrameDecoderServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class DelimiterBasedFrameDecoderChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            //delimiter tag
            ByteBuf delimiter = Unpooled.copiedBuffer(Configuration.DELIMITER_DECODER_TAG.getBytes());
            /**
             * Configuration.LINE_BASE_FRAME_DECODER_SIZE single message maximum size
             * if  larger than this previous setting netty will throws too long frame exception
             *
             */
            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(Configuration.LINE_BASE_FRAME_DECODER_SIZE, delimiter));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoderServerHandler());
        }
}