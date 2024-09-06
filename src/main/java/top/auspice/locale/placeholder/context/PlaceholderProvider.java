package top.auspice.locale.placeholder.context;

@FunctionalInterface
public interface PlaceholderProvider {
    PlaceholderProvider EMPTY = (var0) -> null;

    Object providePlaceholder(String var1);
}
