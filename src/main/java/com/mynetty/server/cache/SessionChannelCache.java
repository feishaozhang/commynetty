package com.mynetty.server.cache;


import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 会话列表
 * 缓存所有的会话Channel
 * 线程安全
 */
public class SessionChannelCache {
    private static Logger logger = Logger.getLogger(SessionChannelCache.class);

    private final static Map<String, Channel> channelSessionCache = new ConcurrentHashMap<String, Channel>();
    private final static Map<Long, String > userIdAndChannelIdMapping = new ConcurrentHashMap<Long, String>();

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 添加Channel
     * @param userID channelId 连接的ID
     * @param channel 会话channel
     */
    public static void addSession(long userID ,Channel channel){
        lock.writeLock().lock();
        try{
            ChannelId channelId = channel.id();
            if(userIdAndChannelIdMapping.containsKey(userID)){
                if(userIdAndChannelIdMapping.get(userID).equals(channelId.asLongText())){
                    return ;
                }
            }
            userIdAndChannelIdMapping.put(userID, channelId.asLongText());
            channelSessionCache.put(channelId.asLongText(), channel);
            logger.info("当前的连接数是："+userIdAndChannelIdMapping.size());
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 删除Session
     * @param userId userId
     */
    public static void removeSession(Long userId){
        lock.writeLock().lock();
        try{
            if(userIdAndChannelIdMapping.containsKey(userId)){
                String channelId = userIdAndChannelIdMapping.get(userId);
                userIdAndChannelIdMapping.remove(userId);
                if(channelSessionCache.containsKey(channelId)){
                    channelSessionCache.remove(channelId);
                }
            }
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }

    }


    /**
     *
     * @param channelId
     */
    public static void removeSession(String channelId){
        lock.writeLock().lock();
        try{
        if(channelSessionCache.containsKey(channelId)){
            channelSessionCache.remove(channelId);
            if(userIdAndChannelIdMapping.containsValue(channelId)){
                Collection<String> values = userIdAndChannelIdMapping.values();
                values.remove(channelId);
            }
        }
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * 获取Session
     * @param userId 用户Id
     * @return
     */
    public static Channel getSession(long userId){
        lock.readLock().lock();
        Channel channel = null;
        try{
        if(!userIdAndChannelIdMapping.containsKey(userId)){
            return null;
        }
            String channelId= userIdAndChannelIdMapping.get(userId);
            channel = channelSessionCache.get(channelId);
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.readLock().unlock();
        }
        return channel;
    }

    /**
     * 根据ChannelId获取CHannel
     * @param channelId
     * @return
     */
    public static Channel getSession(String channelId){
        lock.readLock().lock();
        Channel channel = null;
        try{
        if(channelSessionCache.containsKey(channelId)){
            channel =  channelSessionCache.get(channelId);
        }
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.readLock().unlock();
        }
        return channel;
    }

    public static void resetSession(){
        lock.writeLock().lock();
        try{
        channelSessionCache.clear();
        logger.info("Session 被重置");
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }
    }


}
