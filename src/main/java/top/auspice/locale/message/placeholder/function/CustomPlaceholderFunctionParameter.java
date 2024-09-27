package top.auspice.locale.message.placeholder.function;

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


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{name='" + name + "', type=" + type + ", optional=" + optional + '}';
    }
}
