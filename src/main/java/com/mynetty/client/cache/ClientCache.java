package com.mynetty.client.cache;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运行期缓存
 */
public class ClientCache {
    private static final Logger logger = Logger.getLogger(ClientCache.class);
    /**
     * 运行期数据缓存
     */
    private static Map<String, Object> runtimeCache = new ConcurrentHashMap<String, Object>();

    /**
     * 添加缓存数据
     * @param key channelId 连接的ID
     * @param value 会话channel
     */
    public static void addCacheValue(String key ,Object value){
        if(runtimeCache.containsKey(key)){
            if(runtimeCache.get(key) == value){
                return ;
            }
        }
        runtimeCache.put(key, value);
    }

    /**
     * 删除缓存数据
     * @param key
     */
    public static void removeCacheValue(String key){
        if(runtimeCache.containsKey(key)){
            runtimeCache.remove(key);
        }
    }

    /**
     * 获取缓存数据
     * @param key
     * @return
     */
    public static Object getCacheValue(String key){
        if(!runtimeCache.containsKey(key)){
            return null;
        }
        return runtimeCache.get(key);
    }

    /**
     * 获取缓存数据
     * @param key 缓存值
     * @param t 返货类型
     * @param <T>
     * @return T
     */
    public static <T> T getCacheValue(String key, Class<T> t){
        if(!runtimeCache.containsKey(key)){
            return null;
        }
        T value = null;
        try{
            value = (T)(runtimeCache.get(key));
        }catch (Exception e){
            logger.error(e);
        }
        return value;
    }

    /**
     * 重置CacheValue
     */
    public static void resetCacheValue(){
        runtimeCache.clear();
    }
}
