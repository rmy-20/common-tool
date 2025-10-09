package io.github.rmy20.tool.core.collection;

import java.util.Collection;

/**
 * 集合工具类
 *
 * @author sheng
 */
public class CollectionUtil {
    /**
     * 判断集合是否非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
