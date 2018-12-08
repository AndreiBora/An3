package model;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ForkStmt implements IStmt {
    private IStmt stmt;
    private static Integer threadId = 1;
    private final Lock lock = new ReentrantLock(true);
    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) {
        MyIStack<IStmt> stack = new MyStack<>();
        stack.push(this.stmt);

        lock.lock();
        threadId++;
        PrgState newState = new PrgState(stack,state.getSymTable().clone(),state.getOut(),state.getFileTable(),state.getHeap(),threadId);
        lock.unlock();

        return newState;
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
