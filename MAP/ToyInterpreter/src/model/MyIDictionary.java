package model;

import java.util.Collection;

public interface MyIDictionary<K,V>{
    public V put(K key,V value);
    public V remove(K key);
    public V get(K key);
    public Boolean containsKey(K key);
    public Collection<K> keys();
    public Collection<V> values();


}
