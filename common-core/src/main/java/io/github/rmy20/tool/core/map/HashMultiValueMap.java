package io.github.rmy20.tool.core.map;

import java.util.HashMap;

/**
 * Hash MultiValueMap
 *
 * @author sheng
 */
public class HashMultiValueMap<K, V> extends MultiValueMapAdapter<K, V> {
    /**
     * 创建
     */
    public static <K, V> HashMultiValueMap<K, V> create(int initialCapacity) {
        return new HashMultiValueMap<>(initialCapacity);
    }

    public HashMultiValueMap(int initialCapacity) {
        super(new HashMap<>(initialCapacity));
    }
}
