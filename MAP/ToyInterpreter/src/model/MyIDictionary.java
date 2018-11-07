package model;

import java.util.Collection;

public interface MyIDictionary<K,V>{
    V put(K key, V value);
    V remove(K key);
    V get(K key);
    Boolean containsKey(K key);
    Collection<K> keys();
    Collection<V> values();
}
