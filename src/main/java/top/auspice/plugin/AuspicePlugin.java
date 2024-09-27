package top.auspice.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public interface AuspicePlugin {

    @Language("RegExp")
    String ACCEPTED_NAMESPACE_STRING = "[a-zA-Z]{5,20}";
    Pattern ACCEPTED_NAMESPACE_PATTERN = Pattern.compile(ACCEPTED_NAMESPACE_STRING);

    @NonNull String getAddonName();
    @NonNull @org.intellij.lang.annotations.Pattern(ACCEPTED_NAMESPACE_STRING) String getNamespace();
    @NotNull PluginState getState();



    default void registerPlugin() {
        if (getAddonName() == null) {
            throw new NullPointerException("plugin name can not be null");
        }
        if (getNamespace() == null) {
            throw new NullPointerException("plugin namespace can not be null");
        }
        if (!ACCEPTED_NAMESPACE_PATTERN.matcher(getNamespace()).matches()) {
            throw new IllegalStateException("Key string '" + getNamespace() + "' doesn't match: " + ACCEPTED_NAMESPACE_STRING);
        }
        PluginRegistry.register(this);

    }


}
