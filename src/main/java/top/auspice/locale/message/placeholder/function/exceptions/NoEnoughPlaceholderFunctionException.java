package top.auspice.locale.message.placeholder.function.exceptions;

public class NoEnoughPlaceholderFunctionException extends PlaceholderFunctionException{

    public NoEnoughPlaceholderFunctionException() {
        super();
    }

    public NoEnoughPlaceholderFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEnoughPlaceholderFunctionException(Throwable cause) {
        super(cause);
    }

    protected NoEnoughPlaceholderFunctionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoEnoughPlaceholderFunctionException(String message) {
        super(message);
    }
}
