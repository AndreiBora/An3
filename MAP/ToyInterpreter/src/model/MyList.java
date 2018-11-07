package model;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {

    private List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public Boolean add(T item) {
        return this.list.add(item);
    }

    @Override
    public Boolean remove(T item) {
        return this.list.remove(item);
    }

    @Override
    public String toString() {
        StringBuilder istRepr = new StringBuilder("\n");
        for(T stm:list){
            istRepr.append(stm.toString()).append("\n");
        }
        return istRepr.toString();
    }
}
