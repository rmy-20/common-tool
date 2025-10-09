package io.github.rmy20.tool.core.map;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 多值Map
 *
 * @author sheng
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {
    /**
     * 根据Key获取第一个元素
     *
     * @param key Key
     */
    V getFirst(K key);

    /**
     * 单个添加
     *
     * @param key   Key
     * @param value Value
     */
    void add(K key, V value);

    /**
     * 批量添加
     *
     * @param key    Key
     * @param values Value
     */
    void addAll(K key, List<V> values);

    /**
     * 覆盖添加
     *
     * @param key   Key
     * @param value Value
     */
    void set(K key, V value);

    /**
     * 获取Key对应的元素，如果为空则返回空列表
     *
     * @param key Key
     */
    default List<V> getOrEmpty(K key) {
        List<V> vs = get(key);
        return Objects.isNull(vs) ? Collections.emptyList() : vs;
    }
}
