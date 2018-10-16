package com.cellsgame.game.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * File Description.
 *
 * @author Yang
 */
public class Converter {
    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    /**
     * 如果发生异常返回１
     *
     * @param object 整数对象
     * @return 如果发生异常返回１
     */
    public static int toIntOverZero(Object object) {
        try {
            return Integer.parseInt(object.toString().trim());
        } catch (Exception ex) {
            return 1;
        }
    }

    /**
     * 如果发生异常返回１
     *
     * @param object 对象表示的长整数
     * @return 如果发生异常返回１
     */
    public static long toLongOverZero(Object object) {
        try {
            return Long.parseLong(object.toString().trim());
        } catch (Exception ex) {
            return 1;
        }
    }

    /**
     * 如果发生异常返回0
     *
     * @param object 对象表示的整数
     * @return 整数
     */
    public static int toInteger(Object object) {
        try {
            String value = toString(object);
            return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
        } catch (Exception ex) {
            log.error("parse integer value from an object [{}] error", object, ex);
            return 0;
        }
    }

    public static String toString(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

    public static boolean toBoolean(Object obj) {
        return Boolean.parseBoolean(toString(obj));
    }

    public static byte toByte(Object obj) {
        return (byte) toInteger(obj);
    }

    public static char toChar(Object obj) {
        return toString(obj).toCharArray()[0];
    }

    public static double toDouble(Object obj) {
        try {
            return Double.parseDouble(toString(obj));
        } catch (NumberFormatException e) {
            log.error("toDouble Error param[" + obj + "] :: ", e);
        }
        return 0d;
    }

    public static float toFloat(Object obj) {
        try {
            return Float.parseFloat(toString(obj));
        } catch (NumberFormatException e) {
            log.error("toFloat Error param[" + obj + "] :: ", e);
        }
        return 0.0f;
    }

    public static short toShort(Object obj) {
        return (short) toInteger(obj);
    }

    /**
     * 如果发生异常返回0
     *
     * @param object 对象表示Long
     * @return long
     */
    public static long toLong(Object object) {
        try {
            String value = toString(object);
            return StringUtils.isEmpty(value) ? 0 : new BigDecimal(value).longValue();
        } catch (Exception ex) {
            log.error("pare long value from an object error", ex);
            return 0;
        }
    }


    /**
     * 将类对象实例强制转换为某类型数据
     *
     * @param tClass 数据类型描述
     * @param holder 对象实例
     * @param <T>    类型
     * @return 强转之后的数据
     */
    @SuppressWarnings("unchecked")
    public static <T> T as(Class<T> tClass, Object holder) {
        Assert.isAssignable(holder.getClass(), tClass);
        return (T) holder;
    }
}
