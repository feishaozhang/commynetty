package com.mynetty.client.channeInitializer;

import com.mynetty.client.ClientConfiguration;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class FixedLengthBasedFrameChannelInitializer extends ChannelInitializer<Channel> {

    protected void initChannel(Channel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(ClientConfiguration.LINE_BASE_FRAME_DECODER_SIZE));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(ClientConfiguration .FIXED_LENGTH_SIZE));
    }
}
