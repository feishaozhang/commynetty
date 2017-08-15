package com.mynetty.server;

import com.mynetty.client.channeInitializer.MsgpackChannelInitializer;
import com.mynetty.exception.NettyServerException;
import com.mynetty.server.channelHandler.FixedLengthBasedFrameDecoderChildChannelHandler;
import com.mynetty.server.channelHandler.MsgpackChildChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

/**
 * NettyStarter is used to start the Netty Engineer
 */
public class NettyStarter {
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * bind a port
     * @param port
     * @throws NettyServerException
     */
    public void bind(int port)throws NettyServerException{
        /**
         *  1、Config Server Nio Threads group
         *  2、NioEventLoopGroup is a set of thread group
         *  used to process Network event Process
         *  3、bossGroup used to accept socket connection
         *  4、workGroup used to transfer data
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try{
            /**
             * sbs: used to create NIO  server
             * sbs.channel()：set ChannelType as NioserverSocketChannel
             * sbs.childHandler() used to Handle IO events
             */
            ServerBootstrap sbs = new ServerBootstrap();
            sbs.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, Configuration.SO_BACKLOG_SIZE)
                    .childHandler(new MsgpackChildChannelHandler());

            //Asyn waiting util bind port success
            ChannelFuture f  = sbs.bind(port).sync();
            logger.info("Sever get Started");

            //Asyn waiting util close bind port
            f.channel().closeFuture().sync();
            logger.info("Sever get Stoped");

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
