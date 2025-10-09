package io.github.rmy20.tool.core.array;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * 数组相关工具类
 *
 * @author sheng
 */
public class ArrayUtil {
    /**
     * 判断数组是否为空
     *
     * @param array 数组
     */
    public static boolean isEmpty(Object[] array) {
        return length(array) == 0;
    }

    /**
     * 获取数组长度
     *
     * @param array 数组
     */
    public static int length(Object array) {
        return Objects.isNull(array) ? 0 : Array.getLength(array);
    }
}
