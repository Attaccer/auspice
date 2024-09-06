package top.auspice.constants.namespace;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import top.auspice.plugin.AuspicePlugin;
import top.auspice.plugin.PluginRegistry;

import java.util.Objects;
import java.util.regex.Pattern;

public class Namespace<R extends NamespaceContainerRegistry<?>> {
    private final String plugin;
    private final String module;
    private final String key;
    private final int hashCode;

    private static final Pattern KEY_ACCEPTED_STRING = Pattern.compile("[A-Z0-9_]{3,100}");

    public static <R extends NamespaceContainerRegistry<?>> Namespace<R> newNamespace(@NonNull AuspicePlugin plugin, @NonNull R module, @org.intellij.lang.annotations.Pattern("[A-Z0-9_]{3,100}") @NonNull String key) {
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(module);
        Objects.requireNonNull(key);
        if (!KEY_ACCEPTED_STRING.matcher(key).matches()) {
            throw new IllegalStateException("Key string '" + key + "' doesn't match: " + "[A-Z0-9_]{3,100}");
        }
        String pluginStr = plugin.getNamespace();
        String moduleStr = module.getModule();
        return new Namespace<>(pluginStr, moduleStr, key);
    }

    private Namespace(String plugin, String module, String key) {
        this(plugin, module, key, Objects.hash(plugin, module, key));
    }

    private Namespace(String plugin, String module, String key, int hashCode) {
        this.plugin = plugin;
        this.module = module;
        this.key = key;
        this.hashCode = hashCode;
    }

    public static boolean isValidKey(String key) {
        return KEY_ACCEPTED_STRING.matcher(key).matches();
    }

    private static <R extends NamespaceContainerRegistry<?>> int hashCode0(AuspicePlugin plugin, R module, String key) {
        int hash = 5;
        hash = 47 * hash + plugin.hashCode();
        hash = 47 * hash + module.hashCode();
        hash = 47 * hash + key.hashCode();
        return hash;
    }

    @Pure
    public final boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Namespace<?> other)) {
            return false;
        } else {
            return this.plugin.equals(other.plugin) && this.module.equals(other.module) && this.key.equals(other.key);
        }
    }

    @Pure
    public final int hashCode() {
        return this.hashCode;
    }

    @Pure
    public @NonNull String asConfigString() {
        return this.plugin + ':' + this.module + ':' + configString(this.key);
    }

    public String asString() {
        return this.plugin + ':' + this.module + ':' + this.key;
    }

    public static @NonNull Namespace<?> fromString(@NonNull String str) {
        if (str == null) {
            throw new IllegalArgumentException("Cannot get namespace from null or empty string: '" + str + '\'');
        }

        int sep1 = str.indexOf(':');
        if (sep1 == -1) {
            throw new IllegalArgumentException("Cannot get namespace from null or empty string: '" + str + '\'');
        } else {
            int sep2 = str.indexOf(':', sep1 + 1);
            if (sep2 == -1) {
                throw new IllegalArgumentException("Cannot get namespace from null or empty string: '" + str + '\'');
            } else {
                String pluginStr = str.substring(0, sep1);
                String moduleStr = str.substring(sep1 + 1, sep2);
                String key = str.substring(sep2 + 1);
                return new Namespace<>(pluginStr, moduleStr, key);
            }
        }

    }

    public static Namespace<?> fromConfigString(@NonNull String str) {
        if (str == null) {
            throw new IllegalArgumentException("Cannot get namespace from null or empty string: '" + str + '\'');
        }

        int sep1 = str.indexOf(':');
        if (sep1 == -1) {
            throw new IllegalArgumentException("Cannot get namespace from null or empty string: '" + str + '\'');
        } else {
            int sep2 = str.indexOf(':', sep1 + 1);
            if (sep2 == -1) {
                throw new IllegalArgumentException("Cannot get namespace from null or empty string: '" + str + '\'');
            } else {
                String pluginStr = str.substring(0, sep1);
                String moduleStr = str.substring(sep1 + 1, sep2);
                String key = str.substring(sep2 + 1);
                return new Namespace<>(pluginStr, moduleStr, enumString(key));
            }
        }
    }

    private static String configString(String str) {
        char[] chars = str.toCharArray();
        int len = str.length();

        for(int i = 0; i < len; ++i) {
            char ch = chars[i];
            if (ch == '_') {
                chars[i] = '-';
            } else {
                chars[i] = (char)(ch | 32);
            }
        }

        return new String(chars);
    }

    private static String enumString(String str) {

        char[] chars = str.toCharArray();
        int len = str.length();

        for(int i = 0; i < len; ++i) {
            char ch = chars[i];
            if (ch == '-') {
                chars[i] = '_';
            } else {
                chars[i] = (char)(ch & 95);
            }
        }

        return new String(chars);
    }


    public @Nullable R getRegistry() {
        return (R) NamespaceContainerRegistry.a.get(PluginRegistry.getPluginFromNamespace(this.plugin)).get(this.module);
    }

    public R getModule() {
        return (R) NamespaceContainerRegistry.a.get(PluginRegistry.getPluginFromNamespace(this.plugin)).get(this.module);
    }



}
