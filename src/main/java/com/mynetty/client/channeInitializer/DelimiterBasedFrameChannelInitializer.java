package com.mynetty.client.channeInitializer;

import com.mynetty.client.ClientConfiguration;
import com.mynetty.client.handler.DelimiterBasedFrameDecoderHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class DelimiterBasedFrameChannelInitializer extends ChannelInitializer {

    protected void initChannel(Channel socketChannel) throws Exception {
        ByteBuf delimiterBuf = Unpooled.copiedBuffer(ClientConfiguration.DELIMITER_DECODER_TAG.getBytes());
        socketChannel.pipeline().addLast(new
                DelimiterBasedFrameDecoder(ClientConfiguration.LINE_BASE_FRAME_DECODER_SIZE,delimiterBuf));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoderHandler());
    }
}
