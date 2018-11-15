package model;

import java.util.HashMap;
import java.util.Map;

public class Heap<T> implements IHeap<T> {
    private Integer addr = 1;
    private Map<Integer,T> map;

    public Heap() {
        map = new HashMap<>();
    }

    @Override
    public Integer allocate(T value) {
        this.map.put(addr,value);
        return addr++;
    }

    @Override
    public T deallocate(Integer addr) {
        return this.map.remove(addr);
    }

    @Override
    public T readFromAddr(Integer addr) {
        return this.map.get(addr);
    }

    @Override
    public T writeToAddr(Integer addr, T value) {
        return this.map.put(addr,value);
    }

    @Override
    public String toString() {
        String res = "\n";
        for(HashMap.Entry<Integer,T> entry: this.map.entrySet()){
            res +=  entry.getKey().toString() + " -> " + entry.getValue().toString() + "\n";
        }
        return res;
    }
}
