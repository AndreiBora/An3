package model;

public interface IHeap<T> {
    Integer allocate(T value);
    T deallocate(Integer addr);
    T readFromAddr(Integer addr);
    T writeToAddr(Integer addr,T value);
}
