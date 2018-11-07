package exception;

public class FileOpenException extends RuntimeException {
    public FileOpenException() {
    }

    public FileOpenException(String message) {
        super(message);
    }
}
