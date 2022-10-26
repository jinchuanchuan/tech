package com.youcan.tech.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

import java.io.IOException;

/**
 * JsonCacheSerializer
 */
public class JsonCacheSerializer implements CacheSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        // 记录类型信息，用于反序列化
        objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    @Override
    public byte[] serialize(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("serialize error:" + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> c) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(bytes, Object.class);
        } catch (IOException e) {
            throw new SerializationException("deserialize error:" + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> t) throws SerializationException {
        if (string == null || string.length() == 0) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(string, Object.class);
        } catch (IOException e) {
            throw new SerializationException("deserialize error:" + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, TypeReference<T> c) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(bytes, Object.class);
        } catch (IOException e) {
            throw new SerializationException("deserialize error:" + e.getMessage(), e);
        }
    }
}
