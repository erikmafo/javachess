package com.erikmafo.javachess.utils;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by erikf on 12/10/2016.
 */
public class DoubleEnumKeyMap<K1 extends Enum<K1>, K2 extends Enum<K2>, V> {

    private final Class<K2> key2Class;


    private final Map<K1, Map<K2, V>> map;

    public DoubleEnumKeyMap(Class<K2> key2Class, Map<K1, Map<K2, V>> map) {
        this.map = new EnumMap<>(map);
        this.key2Class = key2Class;
    }

    public DoubleEnumKeyMap(Class<K1> key1Class, Class<K2> key2Class) {
        this.map = new EnumMap<>(key1Class);
        this.key2Class = key2Class;
    }


    public V put(K1 key1, K2 key2, V value)  {

        if (value == null) {
            throw new NullPointerException();
        }

        map.putIfAbsent(key1, new EnumMap<>(key2Class));
        return map.get(key1).put(key2, value);
    }

    public V putIfAbsent(K1 key1, K2 key2, V value) {

        if (value == null) {
            throw new NullPointerException();
        }

        map.putIfAbsent(key1, new EnumMap<>(key2Class));
        return map.get(key1).putIfAbsent(key2, value);
    }


    public V get(K1 key1, K2 key2) {
        return getOrDefault(key1, key2, null);
    }


    public V getOrDefault(K1 key1, K2 key2, V defaultValue) {
        return map.getOrDefault(key1, Collections.emptyMap()).getOrDefault(key2, defaultValue);
    }


    public boolean containsKeyCombination(K1 key1, K2 key2) {
        return null != getOrDefault(key1, key2, null);
    }


}
