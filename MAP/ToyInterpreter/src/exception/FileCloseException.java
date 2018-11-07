package exception;

public class FileCloseException extends RuntimeException {
    public FileCloseException() {
    }

    public FileCloseException(String message) {
        super(message);
    }
}
