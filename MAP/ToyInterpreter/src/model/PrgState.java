package model;

import exception.MyStmtExecException;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.util.EmptyStackException;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Integer> symTable;
    private MyIList<Integer> out;
    private IFileTable fileTable;
    private IHeap<Integer> heap;
    private Integer id;



    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String, Integer> symTable, MyIList<Integer> out, IFileTable fileTable, IHeap<Integer> heap,Integer id) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = id;
    }

    public PrgState oneStep() {
        IStmt stmt;
        try {
            stmt = this.exeStack.pop();
        } catch (EmptyStackException e) {
            throw new MyStmtExecException("No more statements to execute");
        }
        return stmt.execute(this);
    }

    public boolean isNotCompleted(){
        return !this.exeStack.isEmpty();
    }

    public IHeap<Integer> getHeap() {
        return heap;
    }

    public void setHeap(IHeap<Integer> heap) {
        this.heap = heap;
    }

    public IFileTable getFileTable() {
        return fileTable;
    }

    public void setFileTable(IFileTable fileTable) {
        this.fileTable = fileTable;
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
    public String toString() {
        return  "\nThread id: " + this.id +
                "\nExeStack: " + exeStack.toString() +
                "\nSymTable: " + symTable.toString() +
                "\nOut: " + out.toString() +
                "\nFileTable: " + fileTable.toString()+
                "\nHeap: " + heap.toString() +
                "\n------------------\n";
    }
}
