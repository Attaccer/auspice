package top.auspice.locale.placeholder.function.exceptions;

public class MultiplePlaceholderFunctionException extends PlaceholderFunctionException {
    public MultiplePlaceholderFunctionException() {
        super();
    }

    public MultiplePlaceholderFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultiplePlaceholderFunctionException(Throwable cause) {
        super(cause);
    }

    protected MultiplePlaceholderFunctionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MultiplePlaceholderFunctionException(String message) {
        super(message);
    }
}
