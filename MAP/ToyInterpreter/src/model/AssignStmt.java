package model;

public class AssignStmt implements IStmt {
    private String id;
    private Exp exp;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return this.id + " = " + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIDictionary<String,Integer> symTbl = state.getSymTable();
        int val = exp.eval(symTbl);
        //If the symbol table contains the value it updates it
        //else it adds it
        symTbl.put(this.id,val);
        return state;
    }
}
