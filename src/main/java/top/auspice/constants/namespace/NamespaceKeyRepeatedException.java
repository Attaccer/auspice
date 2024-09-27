package top.auspice.constants.namespace;

@SuppressWarnings("unused")
public class NamespaceKeyRepeatedException extends RuntimeException {
    public NamespaceKeyRepeatedException() {
        super();
    }

    public NamespaceKeyRepeatedException(String message) {
        super(message);
    }

    public NamespaceKeyRepeatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceKeyRepeatedException(Throwable cause) {
        super(cause);
    }

    protected NamespaceKeyRepeatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
