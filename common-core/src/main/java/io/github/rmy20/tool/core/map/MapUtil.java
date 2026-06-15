package io.github.rmy20.tool.core.map;

import java.util.Map;

/**
 * Map相关
 *
 * @author sheng
 */
public class MapUtil {
    /**
     * 判断Map是否非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
