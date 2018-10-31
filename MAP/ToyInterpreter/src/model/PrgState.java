package model;

import exception.MyStmtExecException;

import java.util.EmptyStackException;

public class PrgState {
    MyIStack<IStmt> exeStack;
    MyIDictionary<String,Integer> symTable;
    MyIList<Integer> out;

    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String, Integer> symTable, MyIList<Integer> out) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
    }

    public PrgState oneStep(){
        IStmt stmt;
        try{
            stmt = this.exeStack.pop();
        }catch (EmptyStackException e){
            throw new MyStmtExecException("No more statements to execute");
        }
        return stmt.execute(this);
    }

    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public MyIDictionary<String, Integer> getSymTable() {
        return symTable;
    }

    public void setSymTable(MyIDictionary<String, Integer> symTable) {
        this.symTable = symTable;
    }

    public MyIList<Integer> getOut() {
        return out;
    }

    public void setOut(MyIList<Integer> out) {
        this.out = out;
    }

    @Override
    public String toString()
    {
        return  "\nExeStack:" + exeStack.toString() +
                "\nSymTable:"+ symTable.toString() +
                "\nOut:"+ out.toString() +
                "\n------------------\n";
    }
}
