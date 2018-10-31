package exception;

public class MyStmtExecException extends RuntimeException {
    public MyStmtExecException() {
    }

    public MyStmtExecException(String message) {
        super(message);
    }
}
