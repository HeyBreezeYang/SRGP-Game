package com.cellsgame.game.core.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.cellsgame.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by alyx on 17-6-30.
 * 系统默认参数配置
 */
@SuppressWarnings("ALL")
public class GameSysConfig {

    private static final Logger _log = LoggerFactory.getLogger(GameSysConfig.class);
    // 加载
    private static Map<String, String> pros = new HashMap<>();

    static {
        load("/gameSys.properties");
    }

    public static void load(String fileName) {
        Enumeration<URL> resources;
        try {
            Properties properties = System.getProperties();
            load(properties);
            Properties pro = new Properties();
            try (InputStreamReader inputStreamReader = new InputStreamReader(GameSysConfig.class.getResourceAsStream(fileName), Charset.defaultCharset())) {
                pro.load(inputStreamReader);
            }
            load(pro);
        } catch (IOException e) {
            _log.warn("配置加载失败", e);
        }
    }

    private static void load(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (StringUtils.isEmpty(value)) continue;
            String old = pros.put(key, value);
            if (null != old) {
                _log.warn(" 加载重复配置: key: [{}] newVal:[{}] oldVal:[{}]", key, value, old);
            }
        }
    }

    public static boolean containskey(String key) {
        return pros.containsKey(key);
    }

    public static String getValue(String key) {
        String value = pros.get(key);
        return value != null ? value.trim() : "";
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    private static void logUseDefaultVal(String key, Object defaultValue) {
        _log.warn("warn missing property for key: {} using default value: {}", key, defaultValue);
    }

    public static byte getByte(String key, byte defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    public static short getShort(String key, short defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    public static int getInt(String key, int defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    public static long getLong(String key, long defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    public static float getFloat(String key, float defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    public static double getDouble(String key, double defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    public static String getString(String key, String defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static <T extends Enum<T>> T getEnum(String key, Class<T> clazz, T defaultValue) {
        String value = getValue(key);
        if (StringUtil.isEmpty(value)) {
            logUseDefaultVal(key, defaultValue);
            return defaultValue;
        }
        try {
            return Enum.valueOf(clazz, value);
        } catch (IllegalArgumentException e) {
            logErrorVal(key, defaultValue, value);
            return defaultValue;
        }
    }

    private static void logErrorVal(String key, Object defaultValue, String value) {
        _log.warn("warnInvalid value specified for key: {} specified value: {} should be \"{}\" using default value: " + key, value, defaultValue.getClass().getSimpleName(), defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String key, Class<T> superClazz, Class<? extends T> defaultVal) {
        Class<? extends T> clazz = getClazz(key, superClazz, defaultVal);
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logErrorVal(key, defaultVal, clazz.getName());
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getClazz(String key, Class<T> superClazz, Class<? extends T> defaultVal) {
        String clazzName = getValue(key);
        try {
            Class<?> clazz;
            if (StringUtils.isNotEmpty(clazzName)) {
                clazz = ClassLoader.getSystemClassLoader().loadClass(clazzName);
            } else {
                logUseDefaultVal(key, defaultVal);
                clazz = defaultVal;
            }
            Assert.isAssignable(superClazz, clazz, "sysConfig : key:" + key);
            return (Class<? extends T>) clazz;
        } catch (Exception e) {
            logErrorVal(key, defaultVal, clazzName);
            return defaultVal;
        }

    }
}
