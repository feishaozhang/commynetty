package com.mynetty.client;

import com.mynetty.client.cache.ClientCache;
import com.mynetty.client.channeInitializer.MsgpackChannelInitializer;
import com.mynetty.engineerModule.BaseComponentStarter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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
    private static Logger logger = Logger.getLogger(Client.class);
    private static Channel clientChannelHandler = null;
    public static void main(String[] args){
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        //start base engineer
        bStarter.start(ClientConfiguration.log4jPath);


        final Client client = new Client();
        client.initUserInfo();
        client.connect(ClientConfiguration.SERVER_HOSET,ClientConfiguration.SERVER_PORT);

//        while (true){
//            System.out.println("请输入消息");
//            Scanner s = new Scanner(System.in);
//            String line  = s.nextLine();
//            ProtocolMessage pm = MessageTool.getProtocolMessage(line,1000L,2000L, MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
//            clientChannelHandler.writeAndFlush(pm);
//        }
    }

    public Client() {

    }

    public void initUserInfo(){
        //将用户ID放入缓存
        ClientCache.addCacheValue("useId", 1000);
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
            logger.info("SocketChannel has been Created");

            cf.channel().closeFuture().sync();
            logger.info("SocketChannel has benn Closed");

        }catch (Exception e){
        }finally {
            group.shutdownGracefully();
        }
    }


}
