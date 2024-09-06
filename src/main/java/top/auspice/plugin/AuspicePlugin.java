package top.auspice.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.regex.Pattern;

public interface AuspicePlugin {

    Pattern PLUGIN_NAMESPACE_ACCEPTED_STRING = Pattern.compile("[a-zA-Z]{5,20}");

    @org.intellij.lang.annotations.Pattern("[A-Za-z]{3,20}") String getNamespace();
    @NonNull String getAddonName();


    default void registerPlugin() {
        if (getAddonName() == null) {
            throw new NullPointerException("plugin name can not be null");
        }
        if (getNamespace() == null) {
            throw new NullPointerException("plugin namespace can not be null");
        }
        if (!PLUGIN_NAMESPACE_ACCEPTED_STRING.matcher(getNamespace()).matches()) {
            throw new IllegalStateException("Key string '" + getNamespace() + "' doesn't match: " + "[a-zA-Z]{5,20}");
        }
        PluginRegistry.register(this);

    }


}
