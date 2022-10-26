package com.youcan.tech.config.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.youcan.tech.config.CacheSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * CacheTemplateClusterImpl
 */
public class CacheTemplateClusterImpl implements CacheTemplate {
    private static final Logger logger = LoggerFactory.getLogger("cacheDataLog");

    private final JedisCluster jedisCluster;
    private final CacheSerializer cacheSerializer;

    public CacheTemplateClusterImpl(JedisCluster jedisCluster, CacheSerializer cacheSerializer) {
        this.jedisCluster = jedisCluster;
        this.cacheSerializer = cacheSerializer;
    }

    @Override
    public void set(String key, Object value) {
        set(key, value, -1, null);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        long begin = System.currentTimeMillis();
        int seconds = -1;
        if (timeout >= 0 && unit != null) {
            seconds = (int) unit.toSeconds(timeout);
        }
        try {
            byte[] bytes = cacheSerializer.serialize(value);
            if (bytes != null) {
                if (seconds > 0) {
                    jedisCluster.setex(key.getBytes(StandardCharsets.UTF_8), seconds, bytes);
                } else {
                    jedisCluster.set(key.getBytes(StandardCharsets.UTF_8), bytes);
                }
                long end = System.currentTimeMillis();
                logger.info("set key:" + key + "," + (end - begin) + "ms");
            }
        } catch (Exception e) {
            logger.error("failed to set key: " + key, e);
        }
    }

    @Override
    public void mset(Map<String, Object> entries) {

    }

    @Override
    public void mset(Map<String, Object> entries, long timeout, TimeUnit unit) {

    }

    @Override
    public <T> T get(String key, Class<T> c) {
        long begin = System.currentTimeMillis();
        T value = null;
        try {
            byte[] bytes = jedisCluster.get(key.getBytes(StandardCharsets.UTF_8));
            if (bytes != null) {
                value = cacheSerializer.deserialize(bytes, c);
                long end = System.currentTimeMillis();
                logger.info("get key:" + key + "," + (end - begin) + "ms");
                return value;
            }
        } catch (Exception e) {
            logger.error("failed to get key: " + key, e);
        }
        return null;
    }

    @Override
    public <T> Map<String, T> mget(List<String> keys, TypeReference<T> t) {
        // cluster 不支持 mget ，可能会出现不同槽的问题，所以 循环取
        long begin = System.currentTimeMillis();
        if (!keys.isEmpty()) {
            try {
                Map<String, T> m = new HashMap<String, T>();
                for (String key : keys) {
                    if (key != null) {
                        byte[] bytes = jedisCluster.get(key.getBytes(StandardCharsets.UTF_8));
                        if (bytes != null) {
                            T value = cacheSerializer.deserialize(bytes, t);
                            m.put(key, value);
                        }
                    }
                }
                long end = System.currentTimeMillis();
                logger.info("mget keys:" + keys + "," + (end - begin) + "ms");
                return m;
            } catch (Exception e) {
                logger.error("failed to get key:" + keys.toString(), e);
            }
        }
        return null;
    }

    @Override
    public void delete(String key) {
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            logger.error("failed to get key: " + key, e);
        }
    }

}
