package model;

public class PrintStmt implements IStmt{
    private Exp exp;

    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIList<Integer> out = state.getOut();
        MyIDictionary<String,Integer> symTbl = state.getSymTable();
        IHeap<Integer> heap = state.getHeap();
        out.add(exp.eval(symTbl,heap));

        return state;
    }
}
