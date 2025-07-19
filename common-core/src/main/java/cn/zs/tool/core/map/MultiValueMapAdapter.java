package cn.zs.tool.core.map;

import cn.zs.tool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 多值map实现
 *
 * @author sheng
 */
public class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V> {
    /**
     * 空的MultiValueMap
     */
    public static final MultiValueMap<?, ?> EMPTY = create(Collections.emptyMap());

    /**
     * 返回一个空的MultiValueMap
     */
    @SuppressWarnings("unchecked")
    public static <K, V> MultiValueMap<K, V> empty() {
        return (MultiValueMapAdapter<K, V>) EMPTY;
    }

    /**
     * 用于存储值
     */
    private final Map<K, List<V>> table;

    /**
     * 创建一个MultiValueMap
     *
     * @param table 值
     */
    public static <K, V> MultiValueMap<K, V> create(Map<K, List<V>> table) {
        return new MultiValueMapAdapter<>(table);
    }

    public MultiValueMapAdapter(Map<K, List<V>> table) {
        this.table = Objects.requireNonNull(table, "table不允许为空");
    }

    @Override
    public V getFirst(K key) {
        List<V> vs = table.get(key);
        return CollectionUtil.isEmpty(vs) ? null : vs.get(0);
    }

    @Override
    public void add(K key, V value) {
        List<V> vs = computeIfAbsent(key, k -> new ArrayList<>(1));
        vs.add(value);
    }

    @Override
    public void addAll(K key, List<V> values) {
        if (CollectionUtil.isNotEmpty(values)) {
            List<V> vs = computeIfAbsent(key, k -> new ArrayList<>(values.size()));
            vs.addAll(values);
        }
    }

    @Override
    public void set(K key, V value) {
        List<V> vs = new ArrayList<>(1);
        vs.add(value);
        put(key, vs);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (List<V> valueList : table.values()) {
            if (CollectionUtil.isNotEmpty(valueList)) {
                for (V v : valueList) {
                    if (v == value) {
                        return true;
                    }
                    if (Objects.nonNull(v) && v.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<V> get(Object key) {
        return table.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return table.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
        return table.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        table.putAll(m);
    }

    @Override
    public void clear() {
        table.clear();
    }

    @Override
    public Set<K> keySet() {
        return table.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return table.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return table.entrySet();
    }

    @Override
    public String toString() {
        return table.toString();
    }
}
