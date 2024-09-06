package top.auspice.locale.placeholder.function;

public final class CustomPlaceholderFunctionParameter {

    private final String name;

    private final Class<?> type;

    private final boolean optional;

    public CustomPlaceholderFunctionParameter(String name, Class<?> type, boolean optional) {
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }



}
