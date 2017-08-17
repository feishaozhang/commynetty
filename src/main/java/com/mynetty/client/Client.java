package com.mynetty.client;

import com.mynetty.client.channeInitializer.MsgpackChannelInitializer;
import com.mynetty.client.coderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.ProtocalMessage;
import com.mynetty.engineerModule.BaseComponentStarter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * 客户端启动程序
 */
public class Client {
//    private static Logger logger = Logger.getLogger(this);
    private static Channel clientChannelHandler = null;
    public static void main(String[] args){
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        //start base engineer
        bStarter.start(ClientConfiguration.log4jPath);


        final Client client = new Client();
        new Thread(new Runnable() {
            public void run() {
                client.connect(ClientConfiguration.SERVER_HOSET,ClientConfiguration.SERVER_PORT);
            }
        }).start();

        while (true){
            System.out.println("请输入消息");
            Scanner s = new Scanner(System.in);
            String line  = s.nextLine();
            ProtocalMessage pm = MessageTool.getProtocolMessage(line,1000L,2000L, MessageTypeEnum.MESSAGE_BUSSINESS);
            clientChannelHandler.writeAndFlush(pm);
        }


    }

    public Client() {

    }


    public static void connect(String host, int port ){
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,ClientConfiguration.TIME_OUT_MILLIS)
                    .handler(new MsgpackChannelInitializer());
            ChannelFuture cf = bs.connect(host,port).sync();
            clientChannelHandler = cf.channel();
//            logger.info("SocketChannel has been Created");

            cf.channel().closeFuture().sync();
//            logger.info("SocketChannel has benn Closed");

        }catch (Exception e){
        }finally {
            group.shutdownGracefully();
        }
    }


}
