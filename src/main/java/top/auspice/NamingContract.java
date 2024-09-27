package top.auspice;

import org.intellij.lang.annotations.Language;

@SuppressWarnings("unused")
public class NamingContract {
    @Language("RegExp")
    public static final String NAMESPACE = "[a-zA-Z]{5,20}";
    @Language("RegExp")
    public static final String NAMESPACE_KEY = "[A-Z0-9_]{3,100}";
    @Language("RegExp")
    public static final String CONFIG_INTERNAL_PLACEHOLDER = "[a-zA-Z-]";
    @Language("RegExp")
    public static final String CONFIG_GLOBAL_PLACEHOLDER_NAME = "[a-zA-Z_]";
    @Language("RegExp")
    public static final String LOCALE_ENTRY_PATH = "[A-Za-z0-9-]+((\\.[A-Za-z0-9-]+)+)?";
    @Language("RegExp")
    public static final String LOCALE_ENTRY_PATH_SECTION = "[A-Za-z0-9-]";






}
