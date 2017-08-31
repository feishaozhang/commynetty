package com.mynetty.client.cache;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 运行期缓存
 */
public class ClientCache {
    private static final Logger logger = Logger.getLogger(ClientCache.class);
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 运行期数据缓存
     */
    private static Map<String, Object> runtimeCache = new HashMap<String, Object>();

    /**
     * 添加缓存数据
     * @param key channelId 连接的ID
     * @param value 会话channel
     */
    public static void addCacheValue(String key ,Object value){
        lock.writeLock().lock();
        try{
            if(runtimeCache.containsKey(key)){
                if(runtimeCache.get(key) == value){
                    return ;
                }
            }
            runtimeCache.put(key, value);
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * 删除缓存数据
     * @param key
     */
    public static void removeCacheValue(String key){
        lock.writeLock().lock();
        try {
            if(runtimeCache.containsKey(key)){
                runtimeCache.remove(key);
            }
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * 获取缓存数据
     * @param key
     * @return
     */
    public static Object getCacheValue(String key){
        Object data = null;
        lock.readLock().lock();
        try {
            if(!runtimeCache.containsKey(key)){
                return null;
            }
            data = runtimeCache.get(key);
        }catch (Exception e){
            logger.error(e);
        } finally {
            lock.readLock().unlock();
        }
        return data;
    }

    /**
     * 获取缓存数据
     * @param key 缓存值
     * @param t 返货类型
     * @param <T>
     * @return T
     */
    public static <T> T getCacheValue(String key, Class<T> t){
        lock.readLock().lock();
        T value = null;
        try {
            if(!runtimeCache.containsKey(key)){
                return null;
            }
            try{
                value = t.cast((runtimeCache.get(key)));
            }catch (Exception e){
                logger.error(e);
            }
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.readLock().unlock();
        }

        return value;
    }

    /**
     * 重置CacheValue
     */
    public static void resetCacheValue(){
        lock.writeLock().lock();
        try {
            runtimeCache.clear();
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public static Map<String, Object>  getAll(){
        lock.readLock().lock();
        Map<String, Object> data = null;
        try{
            data = runtimeCache;
        }catch (Exception e){
            logger.error(e);
        }finally {
            lock.readLock().unlock();
        }
       return data;
    }
}
