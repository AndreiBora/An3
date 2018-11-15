package model;

public class NewStmt implements IStmt {
    private String varName;
    private Exp exp;

    public NewStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) {
        Integer value = exp.eval(state.getSymTable(), state.getHeap());
        Integer addr = state.getHeap().allocate(value);
        //add var into symbol table or update if it exists
        state.getSymTable().put(varName, addr);
        return state;
    }

    @Override
    public String toString() {
        return "NewStmt(" + this.varName + " ," + this.exp.toString() + ")";

    }
}
