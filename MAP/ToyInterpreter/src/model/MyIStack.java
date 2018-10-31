package model;

public interface MyIStack<T> {
    T pop();

    T push(T v);

    Boolean isEmpty();

}
