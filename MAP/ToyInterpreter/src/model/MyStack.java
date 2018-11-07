package model;

import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyStack() {
        this.stack = new Stack<>();
    }

    @Override
    public T pop() {
        return this.stack.pop();
    }

    @Override
    public T push(T v) {
        return this.stack.push(v);
    }

    @Override
    public Boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public String toString() {

        String istRepr = "\n";
        for (int i = stack.size() - 1; i >= 0; i--) {
            istRepr += stack.get(i) + "\n";
        }
        return istRepr;
    }
}
