package com.mynetty.client.channeInitializer;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.client.handler.LineBasedFrameDecoderHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class LineBasedFrameChannelInitializer extends ChannelInitializer {

    protected void initChannel(Channel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(ClientConfiguration.LINE_BASE_FRAME_DECODER_SIZE));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new LineBasedFrameDecoderHandler());
    }
}
