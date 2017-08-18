package com.mynetty.server.cache;


import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话列表
 * 缓存所有的会话Channel
 * 线程安全
 */
public class SessionChannelCache {
    private static Logger logger = Logger.getLogger(SessionChannelCache.class);

    private final static Map<String, Channel> channelSessionCache = new ConcurrentHashMap<String, Channel>();
    private final static Map<Long, String > userIdAndChannelIdMapping = new ConcurrentHashMap<Long, String>();

    /**
     * 添加Channel
     * @param userID channelId 连接的ID
     * @param channel 会话channel
     */
    public static void addSession(long userID ,Channel channel){
        ChannelId channelId = channel.id();
        if(userIdAndChannelIdMapping.containsKey(userID)){
            if(userIdAndChannelIdMapping.get(userID) == channelId.asLongText()){
                return ;
            }
        }
        userIdAndChannelIdMapping.put(userID, channelId.asLongText());
        channelSessionCache.put(channelId.asLongText(), channel);
    }

    /**
     * 删除Session
     * @param userId userId
     */
    public static void removeSession(Long userId){
        if(userIdAndChannelIdMapping.containsKey(userId)){
            String channelId = userIdAndChannelIdMapping.get(userId);
            userIdAndChannelIdMapping.remove(userId);
            if(channelSessionCache.containsKey(channelId)){
                channelSessionCache.remove(channelId);
            }
        }
    }


    /**
     *
     * @param channelId
     */
    public static void removeSession(String channelId){
        if(channelSessionCache.containsKey(channelId)){
            channelSessionCache.remove(channelId);
            if(userIdAndChannelIdMapping.containsValue(channelId)){
                Collection<String> values = userIdAndChannelIdMapping.values();
                values.remove(channelId);
            }
        }
    }

    /**
     * 获取Session
     * @param userId 用户Id
     * @return
     */
    public static Channel getSession(long userId){
        if(!userIdAndChannelIdMapping.containsKey(userId)){
            return null;
        }
        String channelId = userIdAndChannelIdMapping.get(userId);
        return channelSessionCache.get(channelId);
    }

    /**
     * 根据ChannelId获取CHannel
     * @param channelId
     * @return
     */
    public static Channel getSession(String channelId){
        if(channelSessionCache.containsKey(channelId)){
            return channelSessionCache.get(channelId);
        }
        return null;
    }

    public static void resetSession(){
        channelSessionCache.clear();
        logger.info("Session 被重置");
    }


}
