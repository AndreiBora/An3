package exception;

public class ReadFromFileException extends RuntimeException {
    public ReadFromFileException() {
    }

    public ReadFromFileException(String message) {
        super(message);
    }
}
