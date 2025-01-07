package com.garlic.agents.llm.utils;

import cn.hutool.core.util.StrUtil;
import com.jayway.jsonpath.DocumentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JsonUtil
 *
 * @author MoChenYa
 * @since 1.0
 */
public class JsonUtil {

    public static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * check json path is validate
     * if json path is blank, return false
     *
     * @param context  DocumentContext
     * @param jsonPath json path
     * @return boolean is validate
     */
    public static boolean checkJsonPath(DocumentContext context, String jsonPath) {
        if (StrUtil.isBlank(jsonPath)) {
            return false;
        }
        try {
            context.read(jsonPath);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * read json string to DocumentContext without exception
     *
     * @param context  DocumentContext
     * @param jsonPath json path
     * @return Object return value
     */
    public static Object safeRead(DocumentContext context, String jsonPath) {
        return safeRead(context, jsonPath, Object.class);
    }

    /**
     * read json string to DocumentContext without exception
     *
     * @param context  DocumentContext
     * @param jsonPath json path
     * @param clazz    return class type
     * @param <T>      T
     * @return T return value
     */
    public static <T> T safeRead(DocumentContext context, String jsonPath, Class<T> clazz) {
        if (StrUtil.isBlank(jsonPath)) {
            return null;
        }
        try {
            return context.read(jsonPath, clazz);
        } catch (Exception ignored) {
            return null;
        }
    }
}
