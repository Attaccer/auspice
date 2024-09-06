package top.auspice.locale.compiler.placeholders;

public enum PlaceholderVisibility {

    /**
     * 对其他插件可见
     */
    PUBLIC,

    /**
     * 仅用于当前插件内部, 其他插件无法使用此占位符
     */
    INTERNAL,



}
