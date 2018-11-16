package model;

import java.util.Map;

public interface IHeap<T> {
    Integer allocate(T value);
    T deallocate(Integer addr);
    T readFromAddr(Integer addr);
    T writeToAddr(Integer addr,T value);
    void setContent(Map<Integer,T> map);
    Map<Integer,T> getContent();
}
