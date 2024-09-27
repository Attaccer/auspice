package top.auspice.locale.message.placeholder.context;

@FunctionalInterface
public interface PlaceholderProvider {
    PlaceholderProvider EMPTY = (var0) -> null;
    Object providePlaceholder(String var1);

}
