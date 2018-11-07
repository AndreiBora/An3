package exception;

public class FileDescriptorNotFoundException extends RuntimeException {
    public FileDescriptorNotFoundException() {
    }

    public FileDescriptorNotFoundException(String message) {
        super(message);
    }
}
