package model;

import java.util.Collection;
import java.util.HashMap;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {

    private HashMap<K,V> map;

    public MyDictionary() {
        this.map = new HashMap<K,V>();
    }

    @Override
    public V put(K key, V value) {
        return this.map.put(key,value);
    }

    @Override
    public V remove(K key) {
        return this.map.remove(key);
    }

    @Override
    public V get(K key) {
        return this.map.get(key);
    }

    @Override
    public Boolean containsKey(K key) {
        return this.map.containsKey(key);
    }

    @Override
    public Collection<K> keys() {
        return this.map.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.map.values();
    }

    @Override
    public String toString() {
        String res = "\n";
        for(HashMap.Entry<K,V> entry: this.map.entrySet()){
            res +=  entry.getKey().toString() + " -> " + entry.getValue().toString() + "\n";
        }
        return res;
    }
}
