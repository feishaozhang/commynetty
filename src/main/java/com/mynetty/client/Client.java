package com.mynetty.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mynetty.client.cache.CacheKey;
import com.mynetty.client.cache.ClientCache;
import com.mynetty.client.channeInitializer.MsgpackChannelInitializer;
import com.mynetty.client.exception.CommynettyClientException;
import com.mynetty.client.listener.ClientCallback;
import com.mynetty.client.listener.ListenerTool;
import com.mynetty.client.model.ClientStartParams;
import com.mynetty.commom.msgpack.encoderTool.MessageSender;
import com.mynetty.commom.msgpack.encoderTool.MessageTool;
import com.mynetty.commom.msgpack.messageEnum.MessageStatusEnum;
import com.mynetty.commom.msgpack.messageEnum.MessageTypeEnum;
import com.mynetty.commom.msgpack.model.Message;
import com.mynetty.commom.msgpack.model.ProtocolMessage;
import com.mynetty.engineerModule.BaseComponentStarter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端启动程序
 */
public class Client {
    private static final Logger logger = Logger.getLogger(Client.class);
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private static Client instance;
    /**客户端是否关闭*/
    private volatile boolean isStop = true;

    private ClientCallback listener;
    private ClientStartParams confiParams;
    /**防并发锁,因为是单例模式，所以只有一个实体类，粒度不用整个类*/
    private static final Object  clientLocker = new Object();

    private Client(){

    }

    /**
     * 获得客户端实例
     */
    public static Client getInstance(){
        if (instance == null) {
            synchronized (clientLocker){
                if(instance == null){
                    instance = new Client();
                }
            }
        }
        return instance;
    }

    /**
     * 外部启动接口
     * @param params
     * @see ClientStartParams
     * @throws CommynettyClientException
     */
    public void start(ClientStartParams params, ClientCallback listener) throws CommynettyClientException{
            if(isStop){
                synchronized (clientLocker){
                if(isStop){
                    if(StringUtils.isBlank(params.getHost())){
                        throw new CommynettyClientException("ClientStartParams's host is Null please fill it up");
                    }
                    if(StringUtils.isBlank(params.getAuth())){
                        throw new CommynettyClientException("ClientStartParams's auth is Null please fill it up");
                    }
                    if(params.getPort() == 0){
                        logger.warn("your server port  is 0!");
                    }
                    if(params.getCrcCode() == 0){
                        logger.warn("your crcCode is 0 !");
                    }

                    this.listener = listener;
                    this.confiParams = params;

                    /**需要设置用户的ID用户校验*/
                    ClientCache.addCacheValue(CacheKey.USER_ID, params.getAuth());

                    startBaseComponent();
                    startConnect(params);
                    isStop = false;
                }
                else{
                    logger.warn("Server is get started No need to start again!");
                }
                }
            }
            else{
                logger.warn("Server is get started No need to start again!");
            }
    }

    /**
     * 外部启动接口
     * @param params
     * @see ClientStartParams
     * @throws CommynettyClientException
     */
    public void startWithTestPattern(ClientStartParams params, ClientCallback listener) throws CommynettyClientException{
        if(StringUtils.isBlank(params.getHost())){
            throw new CommynettyClientException("ClientStartParams' s host is Null please fill it up");
        }
        if(StringUtils.isBlank(params.getAuth())){
            throw new CommynettyClientException("ClientStartParams's auth is Null please fill it up");
        }
        if(params.getPort() == 0){
            logger.warn("your server port is 0!");
        }
        if(params.getCrcCode() == 0){
            logger.warn("your crcCode is 0 !");
        }
        this.listener = listener;
        this.confiParams = params;

        /**重置客户端配置文件*/
        resetClientProperties();
        /**需要设置用户的ID用户校验*/
        ClientCache.addCacheValue(CacheKey.USER_ID, params.getAuth());
        startBaseComponent();
        startConnect(params);
        isStop = false;
    }

    /**
     * 启动基础组件
     */
    public static void startBaseComponent(){
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        bStarter.start(ClientConfiguration.log4jPath);
    }

    /**
     * 连接消息服务器
     */
    public void startConnect(final ClientStartParams params) throws  CommynettyClientException{
            new Thread(new Runnable() {
                public void run() {
                     connect(params.getHost(),params.getPort(), listener);
                }
            }).start();
    }

    /**
     * 初始化以及数据缓存
     */
    public void initUserInfo(ChannelFuture channelFuture){
        ClientCache.addCacheValue(CacheKey.RECONNECT_COUNT,ClientConfiguration.RECONNECT_COUNT);
        ClientCache.addCacheValue(CacheKey.CONNECTED_CHANNEL,channelFuture.channel());
        ClientCache.addCacheValue(CacheKey.CLIENT_STATUS,0);
    }

    /**
     * 连接服务端
     * @param host
     * @param port
     * @throws CommynettyClientException
     */
    public void connect( String host, int port, final ClientCallback listener)throws CommynettyClientException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,ClientConfiguration.TIME_OUT_MILLIS)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new MsgpackChannelInitializer());
            ChannelFuture cf = bs.connect(host,port).sync();
            cf.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        /**连接成功*/
                        initUserInfo( channelFuture );
                        logger.info("SocketChannel has been Created");
                        if(listener != null){
                              ListenerTool.callBack(listener, ClientCallback.OpType.CONNECT_SUCCESS, "SocketChannel connection is Success");
                        }
                    }
                    else{
                        if(listener != null){
                            ListenerTool.callBack(listener, ClientCallback.OpType.CONNECT_FAIL, "SocketChannel connection is  fail");
                        }
                    }
                }
            });

            cf.channel().closeFuture().sync();
            logger.info("SocketChannel has benn Closed");
        }catch (Exception e){
            throw new CommynettyClientException("服务器连接异常");
        }finally {
            try{
                int  clientStatus = (Integer)ClientCache.getCacheValue(CacheKey.CLIENT_STATUS);
//                if(clientStatus == 0){//非退出状态重连){
//                    reconnectToServer(listener);
//                }else{
//                    group.shutdownGracefully();
//                }
            }catch (Exception e){
                throw new CommynettyClientException(e.getMessage());
            }
        }
    }

    /**
     * 重置客户端参数
     */
    public void resetClientProperties(){
        if(ClientCache.getAll().size() > 0){
            ClientCache.resetCacheValue();
        }
    }

    /**
     * 重连
     */
    public void reconnectToServer(final ClientCallback listener)throws CommynettyClientException{
        executor.execute(new Runnable() {
            public void run() {
                while(true) {
                    int reconnectCount = (Integer) ClientCache.getCacheValue(CacheKey.RECONNECT_COUNT);
                   if(reconnectCount > 0)
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        try {
                            connect(ClientConfiguration.SERVER_HOSET, ClientConfiguration.SERVER_PORT, listener);
                            ClientCache.addCacheValue(CacheKey.RECONNECT_COUNT, ClientConfiguration.RECONNECT_COUNT);//连接成功后，还原为默认重连数
                        } catch (Exception e) {
                            e.printStackTrace();
                            ClientCache.addCacheValue(CacheKey.RECONNECT_COUNT, reconnectCount--);//可重连次数递减
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 发送消息
     * @param msg
     */
    public void sendMessage(Message msg){
        Channel channel = (Channel) ClientCache.getCacheValue(CacheKey.CONNECTED_CHANNEL);
        if(channel == null){
            logger.error("未连接服务器!");
            return;
        }
        ProtocolMessage pm = MessageTool.getProtocolMessage(msg.getMessage(),msg.getFrom(),msg.getTarget(), MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
        MessageSender.sendMessage(channel, pm);
    }

    /**
     * 关闭连接
     */
    public void closeConnection() throws CommynettyClientException{
        if(!isStop){
            synchronized (clientLocker){
                if(!isStop){
                    Channel channel = (Channel) ClientCache.getCacheValue(CacheKey.CONNECTED_CHANNEL);
                    channel.eventLoop().shutdownGracefully();
                }
            }
        }
    }
}
