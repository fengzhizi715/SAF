package com.safframework.saf.utils;

import com.safframework.tony.common.utils.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类存在的目的:hashmap可以存key为null或者value为null的
 * 但是这个map不能,保证所有的value都不为空
 * Created by Tony Shen on 16/4/22.
 */
public class NoEmptyHashMap<K,V> extends HashMap<K, V> {

    public NoEmptyHashMap() {
        super();
    }

    public NoEmptyHashMap(final Map<K,V> map) {
        super();
        putAll(map);
    }

    @Override
    public V put(final K key, final V value) {
        if (Preconditions.isBlank(key) || Preconditions.isBlank(value)) {
            return null;
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
