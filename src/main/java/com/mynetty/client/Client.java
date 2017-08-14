package com.mynetty.client;

import com.mynetty.client.handler.LineBasedFrameDecoderHandler;
import com.mynetty.engineerModule.BaseComponentStarter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.log4j.Logger;

public class Client {
    private Logger logger = Logger.getLogger(this.getClass());

    public static void main(String[] args){
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        //start base engineer
        bStarter.start(ClientConfiguration.log4jPath);

        //begin start the Netty Server
        Client client = new Client();
        client.connect(ClientConfiguration.SERVER_HOSET,ClientConfiguration.SERVER_PORT);
    }

    public Client() {

    }


    public void connect(String host, int port ){
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(ClientConfiguration.LINE_BASE_FRAME_DECODER_SIZE));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoderHandler());
                        }
                    });
            ChannelFuture cf = bs.connect(host,port).sync();
            logger.info("SocketChannel has been Created");

            cf.channel().closeFuture().sync();
            logger.info("SocketChannel has benn Closed");

            group.shutdownGracefully();
        }catch (Exception e){

        }
    }


}
