package com.youcan.tech.config.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.youcan.tech.config.CacheSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * CacheTemplateImpl
 */
public class CacheTemplateImpl implements CacheTemplate, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger("cacheDataLog");

    private final JedisPool jedisPool;
    private final CacheSerializer cacheSerializer;

    public CacheTemplateImpl(JedisPool jedisPool, CacheSerializer cacheSerializer) {
        this.jedisPool = jedisPool;
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
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = cacheSerializer.serialize(value);
            if (bytes != null) {
                if (seconds > 0) {
                    jedis.setex(key.getBytes(StandardCharsets.UTF_8), seconds, bytes);
                } else {
                    jedis.set(key.getBytes(StandardCharsets.UTF_8), bytes);
                }
                long end = System.currentTimeMillis();
                logger.info("set key:" + key + "," + (end - begin) + "ms");
            }
        } catch (Exception e) {
            logger.error("failed to set key:" + key, e);
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
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(key.getBytes(StandardCharsets.UTF_8));
            value = cacheSerializer.deserialize(bytes, c);
            long end = System.currentTimeMillis();
            logger.info("get key:" + key + "," + (end - begin) + "ms");
        } catch (Exception e) {
            logger.error("failed to get key:" + key, e);
        }
        return value;
    }

    @Override
    public <T> Map<String, T> mget(List<String> keys, TypeReference<T> t) {
        long begin = System.currentTimeMillis();
        if (!keys.isEmpty()) {
            try (Jedis jedis = jedisPool.getResource()) {
                List<String> values = jedis.mget(keys.toArray(new String[keys.size()]));
                Map<String, T> m = new HashMap<String, T>();
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    String value = values.get(i);
                    m.put(key, cacheSerializer.deserialize(value, t));
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
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("failed to delete key:" + key, e);
        }
    }

    @Override
    public void destroy() {
        jedisPool.destroy();
    }
}
