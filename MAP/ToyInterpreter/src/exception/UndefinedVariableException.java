package exception;

public class UndefinedVariableException extends RuntimeException {
    public UndefinedVariableException() {
    }

    public UndefinedVariableException(String message) {
        super(message);
    }
}
