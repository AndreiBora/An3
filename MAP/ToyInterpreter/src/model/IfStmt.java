package model;

public class IfStmt implements IStmt{
    private Exp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(Exp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public String toString() {
        return "if (" + exp.toString() + ") then " + thenS.toString() + " else " + elseS.toString() + " end ";
    }

    @Override
    public PrgState execute(PrgState state) {
        if(exp.eval(state.getSymTable(),state.getHeap()) != 0){
            state.getExeStack().push(thenS);
        }else{
            state.getExeStack().push(elseS);
        }
        return null;
    }
}
