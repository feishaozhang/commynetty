package com.mynetty.server.cache;

import com.mynetty.commom.msgpack.model.ProtocalMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存未成功转发的消息
 */
public class MessageCache {

    private final Map<String, ProtocalMessage> messageCache = new HashMap<String, ProtocalMessage>();

    public void put(String userId, ProtocalMessage message){
        synchronized (messageCache){
            messageCache.put(userId,message);
        }
    }

    /**
     * 获取消息Cache
     * @param key
     * @return
     */
    public ProtocalMessage getMessageCache(String key){
        synchronized (messageCache){
           return  messageCache.get(key);
        }
    }

    /**
     * 清空消息Cache
     */
    public void resetMessageCache(){
        synchronized (messageCache){
            messageCache.clear();
        }
    }
}
