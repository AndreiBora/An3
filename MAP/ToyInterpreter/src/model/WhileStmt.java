package model;

public class WhileStmt implements IStmt{
    private Exp exp;
    private IStmt stmt;

    public WhileStmt(Exp exp, IStmt stmt) {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIStack stack = state.getExeStack();
        if(exp.eval(state.getSymTable(),state.getHeap()) != 0){
            stack.push(this);
            stack.push(stmt);
        }
        return null;
    }

    @Override
    public String toString() {
        return "While (" + this.exp.toString() + ") do " + this.stmt.toString() + " end ";
    }
}
