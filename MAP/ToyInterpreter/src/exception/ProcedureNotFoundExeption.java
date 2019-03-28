package exception;

public class ProcedureNotFoundExeption extends RuntimeException {
    public ProcedureNotFoundExeption() {
    }

    public ProcedureNotFoundExeption(String message) {
        super(message);
    }
}
