package com.mynetty.client;

import com.mynetty.client.channeInitializer.DelimiterBasedFrameChannelInitializer;
import com.mynetty.client.channeInitializer.MsgpackChannelInitializer;
import com.mynetty.engineerModule.BaseComponentStarter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

/**
 * 客户端启动程序
 */
public class Client {
    private Logger logger = Logger.getLogger(this.getClass());

    public static void main(String[] args){
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        //start base engineer
        bStarter.start(ClientConfiguration.log4jPath);


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
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,ClientConfiguration.TIME_OUT_MILLIS)
                    .handler(new MsgpackChannelInitializer());
            ChannelFuture cf = bs.connect(host,port).sync();
            logger.info("SocketChannel has been Created");

            cf.channel().closeFuture().sync();
            logger.info("SocketChannel has benn Closed");

        }catch (Exception e){
        }finally {
            group.shutdownGracefully();
        }
    }


}
