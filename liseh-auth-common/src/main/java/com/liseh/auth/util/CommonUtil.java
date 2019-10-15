package com.liseh.auth.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class CommonUtil {
    private static Logger logger = LogManager.getLogger(CommonUtil.class);
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static  <T> T getObject(String source, Class<T> clazz) {
        try {
            return mapper.readValue(source, clazz);
        } catch (Exception ex) {
            LogUtils.error(logger, "GetObject", "Failed", ex.getMessage());
            return null;
        }
    }

    public static String getJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception ex) {
            LogUtils.error(logger, "GetJsonString", "Failed", ex.getMessage());
            return "";
        }
    }
}
