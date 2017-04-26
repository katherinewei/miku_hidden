package com.hiden.biz.memcached;

import net.spy.memcached.MemcachedClient;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * Created by myron on 16-9-30.
 */
public class MemcachedCacheManager implements CacheManager {

    private MemcachedClient memcachedClient;

    private int expireSecond;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new MemcachedDelegator<K, V>(name, memcachedClient, expireSecond);
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public void setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
    }
}
