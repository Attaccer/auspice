package top.auspice.locale.message.placeholder.function.exceptions;

public abstract class PlaceholderFunctionParameterException extends RuntimeException {
    public PlaceholderFunctionParameterException() {
        super();
    }

    public PlaceholderFunctionParameterException(String message) {
        super(message);
    }

    public PlaceholderFunctionParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaceholderFunctionParameterException(Throwable cause) {
        super(cause);
    }

    protected PlaceholderFunctionParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
