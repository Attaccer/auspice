package top.auspice.locale.message.placeholder.function.exceptions;

public abstract class PlaceholderFunctionException extends Exception {
    public PlaceholderFunctionException() {
        super();
    }

    public PlaceholderFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaceholderFunctionException(Throwable cause) {
        super(cause);
    }

    protected PlaceholderFunctionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PlaceholderFunctionException(String message) {
        super(message);
    }
}
