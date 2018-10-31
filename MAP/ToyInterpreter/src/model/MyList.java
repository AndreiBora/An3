package model;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {

    private List<T> list;

    public MyList() {
        this.list = new ArrayList<T>();
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
        String istRepr = "";
        for(T stm:list){
            istRepr += stm.toString() + "\n";
        }
        return istRepr;
    }
}
