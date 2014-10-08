package com.withub.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类.
 */
public final class CollectionUtil {

    /**
     * 默认分割符
     */
    public static final String DEFAULT_SEPARATOR = ",";

    /**
     * 对象属性分隔符
     */
    public static final String PROPERTY_SEPARATOR = ".";

    /**
     * 判断字节数组是否为空
     *
     * @param array 字节数组
     * @return boolean
     */
    public static boolean isEmpty(final byte[] array) {

        return array == null || array.length == 0;
    }

    /**
     * 判断字节数组是否为非空
     *
     * @param array 字节数组
     * @return boolean
     */
    public static boolean isNotEmpty(final byte[] array) {

        return array != null && array.length > 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组对象
     * @return boolean
     */
    public static boolean isEmpty(final Object[] array) {

        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为非空
     *
     * @param array 数组对象
     * @return boolean
     */
    public static boolean isNotEmpty(final Object[] array) {

        return array != null && array.length > 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合对象
     * @return boolean
     */
    public static boolean isEmpty(final Collection<?> collection) {

        return collection == null || collection.size() == 0;
    }

    /**
     * 判断集合是否非空
     *
     * @param collection 集合对象
     * @return boolean
     */
    public static boolean isNotEmpty(final Collection<?> collection) {

        return collection != null && collection.size() > 0;
    }

    /**
     * 判断 Map 是否为空
     *
     * @param map Map 对象
     * @return boolean
     */
    public static boolean isEmpty(final Map<?, ?> map) {

        return map.isEmpty();
    }

    /**
     * 判断 Map 是否非空
     *
     * @param map Map 对象
     * @return boolean
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {

        return !map.isEmpty();
    }

}
