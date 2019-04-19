package IO;

public class IllegalInputFileException extends Exception {

    public IllegalInputFileException() {
        super();
    }

    public IllegalInputFileException(String message) {
        super(message);
    }

    public IllegalInputFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInputFileException(Throwable cause) {
        super(cause);
    }

}
