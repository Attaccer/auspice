package top.auspice.config.placeholder;

@FunctionalInterface
public interface InternalPlaceholderProvider {
    InternalPlaceholderProvider EMPTY = varName -> null;
    Object providePlaceholder(String varName);

    PlaceholderType type = PlaceholderType.INTERNAL;
}
