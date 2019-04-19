package IO;

public class IllegalConfigurationArgumentsException extends Exception {

    public IllegalConfigurationArgumentsException() {
        super();
    }

    public IllegalConfigurationArgumentsException(String message) {
        super(message);
    }

    public IllegalConfigurationArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalConfigurationArgumentsException(Throwable cause) {
        super(cause);
    }

}
