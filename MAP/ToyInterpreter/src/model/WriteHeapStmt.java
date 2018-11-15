package model;

public class WriteHeapStmt implements IStmt {
    private String varName;
    private Exp exp;

    public WriteHeapStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) {
        IHeap<Integer> heap = state.getHeap();
        MyIDictionary<String,Integer> symTbl = state.getSymTable();
        Integer value = exp.eval(symTbl,heap);

        //check if the memory is allocated by performing a read
        Exp readHeap = new ReadHeapExp(varName);
        readHeap.eval(symTbl,heap);

        Integer addr = symTbl.get(varName);
        heap.writeToAddr(addr,value);

        return null;
    }

    @Override
    public String toString() {
        return "wh (" + this.varName + ", " + this.exp.toString() + ")";
    }
}
