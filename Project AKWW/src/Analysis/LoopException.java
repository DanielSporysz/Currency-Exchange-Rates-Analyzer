package Analysis;

public class LoopException extends Exception {

    public LoopException() {
        super();
    }

    public LoopException(String message) {
        super(message);
    }

    public LoopException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoopException(Throwable cause) {
        super(cause);
    }

}
