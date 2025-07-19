package cn.zs.tool.core.map;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 链表类型的MultiValueMap
 *
 * @author sheng
 */
public class LinkedMultiValueMap<K, V> extends MultiValueMapAdapter<K, V> implements Serializable {
    private static final long serialVersionUID = -6868354241101680359L;

    /**
     * 创建LinkedMultiValueMap
     *
     * @param initialCapacity 初始容量
     */
    public static <K, V> LinkedMultiValueMap<K, V> create(int initialCapacity) {
        return new LinkedMultiValueMap<>(initialCapacity);
    }

    public LinkedMultiValueMap(int initialCapacity) {
        super(new LinkedHashMap<>(initialCapacity));
    }
}
