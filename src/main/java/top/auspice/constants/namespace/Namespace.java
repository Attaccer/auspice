package top.auspice.constants.namespace;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class Namespace<C extends Namespaced<C, R>, R extends NamespacedRegistry<C, R>> {
    private final R registry;
    private final String namespace;
    private final @org.intellij.lang.annotations.Pattern(ACCEPTED_KEY_STRING) String key;
    private final int hashCode;

    public static final String ACCEPTED_KEY_STRING = "[A-Z0-9_]{3,100}";
    public static final Pattern ACCEPTED_KEY_PATTERN = Pattern.compile(ACCEPTED_KEY_STRING);

    public Namespace(R registry, String namespace, String key) {
        this(registry, namespace, key, Objects.hash(namespace, key));
    }

    private Namespace(R registry, String namespace, @org.intellij.lang.annotations.Pattern(ACCEPTED_KEY_STRING) String key, int hashCode) {
        Objects.requireNonNull(registry);
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(key);
        if (!ACCEPTED_KEY_PATTERN.matcher(key).matches()) {
            throw new IllegalStateException("Key string '" + key + "' doesn't match: " + "[A-Z0-9_]{3,100}");
        }
        this.registry = registry;
        this.namespace = namespace;
        this.key = key;
        this.hashCode = hashCode;
    }

    public static boolean isValidKey(String key) {
        return ACCEPTED_KEY_PATTERN.matcher(key).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace<C, R> namespace1 = (Namespace) o;
        return hashCode == namespace1.hashCode && Objects.equals(namespace, namespace1.namespace) && Objects.equals(key, namespace1.key);
    }

    @Pure
    public int hashCode() {
        return this.hashCode;
    }

    @Pure
    public @NonNull String asConfigString(boolean abbreviate) {
        if (abbreviate) {
            for (Namespace<C, R> ns : this.registry.getRegistry().keySet()) {
                if (ns != this && ns.getKey().equals(this.getKey())) {       //若找到任何key相同的命名空间
                    return this.namespace + ':' + configString(this.key);
                }
            }
            return configString(this.getKey());
        } else {
            return this.namespace + ':' + configString(this.key);
        }
    }

    public String asString() {
        return this.namespace + ':' + this.key;
    }

    static String configString(String str) {
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

    static String enumString(String str) {

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

    public R getRegistry() {
        return registry;
    }

    public String getNamespace() {
        return this.namespace;
    }
    public String getKey() {
        return this.key;
    }

}
