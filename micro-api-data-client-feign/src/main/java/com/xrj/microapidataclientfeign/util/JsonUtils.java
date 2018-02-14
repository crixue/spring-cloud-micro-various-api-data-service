package com.xrj.microapidataclientfeign.util;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String encode(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 将json string反序列化成对象
     *
     * @param json
     * @param valueType
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public static <T> T decode(String json, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
    	objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false); //不识别的字段不会抛出异常
        return objectMapper.readValue(json, valueType);
    }

    /**
     * 将json array反序列化为对象
     *
     * @param json
     * @param jsonTypeReference
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public static <T> T decode(String json, TypeReference<T> jsonTypeReference) throws JsonParseException, JsonMappingException, IOException {
    	objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false); //不识别的字段不会抛出异常
        return (T) objectMapper.readValue(json, jsonTypeReference);
    }
}
