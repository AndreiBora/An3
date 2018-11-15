package model;

import exception.UndefinedVariableException;

public class VarExp extends Exp{
    private String id;

    public VarExp(String id) {
        this.id = id;
    }

    @Override
    int eval(MyIDictionary<String, Integer> tbl,IHeap<Integer> heap) {
        Integer res = tbl.get(this.id);
        if(res == null){
            throw new UndefinedVariableException(this.id + " must be declared before");
        }
        return res;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
