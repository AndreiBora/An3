package model;

import exception.HeapException;
import exception.UndefinedVariableException;

public class ReadHeapExp extends Exp {
    private String varName;

    public ReadHeapExp(String varName) {
        this.varName = varName;
    }

    @Override
    int eval(MyIDictionary<String, Integer> tbl, IHeap<Integer> heap) {
        Integer addr = tbl.get(varName);
        if (addr == null) {
            throw new UndefinedVariableException("Variable " + this.varName + " must be defined before use");
        }
        Integer val = heap.readFromAddr(addr);
        if (val == null) {
            throw new HeapException(val + " is not a valid location");
        }
        return val;
    }

    @Override
    public String toString() {
        return "rh( " + this.varName + " )";
    }
}
