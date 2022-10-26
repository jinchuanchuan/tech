package com.youcan.tech.config;


import com.fasterxml.jackson.core.type.TypeReference;

/**
 * CacheSerializer
 */
public interface CacheSerializer {

    byte[] serialize(Object data) throws SerializationException;

    <T> T deserialize(byte[] bytes, Class<T> c) throws SerializationException;

    <T> T deserialize(byte[] bytes, TypeReference<T> c) throws SerializationException;

    <T> T deserialize(String string, TypeReference<T> c) throws SerializationException;

}
