package com.mynetty.server;

import com.mynetty.exception.CommyNettyServerException;
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
     * @throws CommyNettyServerException
     */
    public void bind(int port)throws CommyNettyServerException {
        //Acceptor Thread pool
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //write & read Thread pool
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap sbs = new ServerBootstrap();
            sbs.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, Configuration.SO_BACKLOG_SIZE)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new MsgpackChildChannelHandler());

            //同步绑定端口
            ChannelFuture f  = sbs.bind(port).sync();
            logger.info("Sever get Started");

            //同步关闭通道
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
