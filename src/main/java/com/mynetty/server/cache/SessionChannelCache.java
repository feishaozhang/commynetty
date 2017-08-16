package com.mynetty.server.cache;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话列表
 * 缓存所有的会话Channel
 * 线程安全
 */
public class SessionChannelCache {

    private final static Map<Integer, Channel> sessionCache = new ConcurrentHashMap<Integer, Channel>();

    /**
     * 添加Channel
     * @param channelId channelId 连接的ID
     * @param channel 会话channel
     */
    public static void addSession(int channelId ,Channel channel){
        if(sessionCache.containsKey(channelId)){
            if(sessionCache.get(channelId) == channel){
                return ;
            }
        }
        sessionCache.put(channelId, channel);
    }

    /**
     * 删除Session
     * @param channelId
     */
    public static void removeSession(int channelId){
        if(sessionCache.containsKey(channelId)){
            sessionCache.remove(channelId);
        }
    }

    /**
     * 获取Session
     * @param channelId
     * @return
     */
    public static Channel getSession(int channelId){
        if(!sessionCache.containsKey(channelId)){
            return null;
        }
        return sessionCache.get(channelId);
    }
}
