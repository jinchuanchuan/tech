package com.youcan.tech.config;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.SerializationUtils;

/**
 * JdkCacheSerializer
 */
public class JdkCacheSerializer implements CacheSerializer {
    @Override
    public byte[] serialize(Object data) {
        return SerializationUtils.serialize(data);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> c) {
        return (T) SerializationUtils.deserialize(bytes);
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> c) throws SerializationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T deserialize(byte[] bytes, TypeReference<T> c) throws SerializationException {
        // TODO Auto-generated method stub
        return null;
    }

}
