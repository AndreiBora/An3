package model;

abstract class Exp {
    abstract int eval(MyIDictionary<String,Integer> tbl,IHeap<Integer> heap);
}
