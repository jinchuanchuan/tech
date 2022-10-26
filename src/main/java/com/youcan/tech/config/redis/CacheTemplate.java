package com.youcan.tech.config.redis;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface CacheTemplate {
    void set(String key, Object value);

    void set(String key, Object value, long timeout, TimeUnit unit);

    void mset(Map<String, Object> entries);

    void mset(Map<String, Object> entries, long timeout, TimeUnit unit);

    <T> T get(String key, Class<T> c);

    <T> Map<String, T> mget(List<String> key, TypeReference<T> t);

    void delete(String key);
}
