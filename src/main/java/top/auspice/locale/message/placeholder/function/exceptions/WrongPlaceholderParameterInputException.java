package top.auspice.locale.message.placeholder.function.exceptions;

public class WrongPlaceholderParameterInputException extends PlaceholderFunctionParameterException {
    public WrongPlaceholderParameterInputException() {
        super();
    }

    public WrongPlaceholderParameterInputException(String message) {
        super(message);
    }

    public WrongPlaceholderParameterInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPlaceholderParameterInputException(Throwable cause) {
        super(cause);
    }

    protected WrongPlaceholderParameterInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
