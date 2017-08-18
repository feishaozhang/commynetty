package com.mynetty.client;

import com.mynetty.client.cache.ClientCache;
import com.mynetty.client.channeInitializer.MsgpackChannelInitializer;
import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import com.mynetty.engineerModule.BaseComponentStarter;
import com.mynetty.exception.CommyNettyServerException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 客户端启动程序
 */
public class Client {
    private static Logger logger = Logger.getLogger(Client.class);
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void main(String[] args){
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        //start base engineer
        bStarter.start(ClientConfiguration.log4jPath);

        final Client client = new Client();
//        for (int i=0; i< 300;i++){
            new Thread(new Runnable() {
                public void run() {
                    try{
                        client.initUserInfo();
                        client.connect(ClientConfiguration.SERVER_HOSET,ClientConfiguration.SERVER_PORT);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
//            try {
//                Thread.sleep(100);
//            }catch (Exception e){
//            }
//        }
        client.startWriter();
    }

    /**
     * 发送文字板
     */
    public void startWriter(){
        try{
            while (true){
                System.out.println("请输入消息");
                Scanner s = new Scanner(System.in);
                String line  = s.nextLine();
                long userId = Long.parseLong(ClientCache.getCacheValue("userId",String.class));
                ProtocolMessage pm = MessageTool.getProtocolMessage(line,userId,2000, MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
                Channel channel = (Channel)ClientCache.getCacheValue("channel");
                MessageSender.sendMessage(channel,pm);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initUserInfo(){
        //将用户ID放入缓存
        ClientCache.addCacheValue("userId", "1000");
    }

    public static void connect(String host, int port )throws CommyNettyServerException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,ClientConfiguration.TIME_OUT_MILLIS)
                    .handler(new MsgpackChannelInitializer());
            ChannelFuture cf = bs.connect(host,port).sync();
            cf.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    ClientCache.addCacheValue("channel",channelFuture.channel());
                    ClientCache.addCacheValue("clientStatus",0);

                    logger.info("SocketChannel has been Created");
                }
            });


            cf.channel().closeFuture().sync();
            logger.info("SocketChannel has benn Closed");
        }catch (Exception e){
            throw new CommyNettyServerException("服务器连接异常");
        }finally {
            executor.execute(new Runnable() {
                public void run() {
                    int  clientStatus = (Integer)ClientCache.getCacheValue("clientStatus");
                    if(clientStatus == 0){//非退出状态重连
                        try {
                            TimeUnit.SECONDS.sleep(5);
                            try{
                                connect(ClientConfiguration.SERVER_HOSET,ClientConfiguration.SERVER_PORT);
                            }catch (Exception e){
                                e.printStackTrace();
                                throw new CommyNettyServerException("服务器连接异常");
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                }
            });

            group.shutdownGracefully();
        }
    }
}
