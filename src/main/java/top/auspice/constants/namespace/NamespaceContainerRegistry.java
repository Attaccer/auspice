package top.auspice.constants.namespace;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import top.auspice.plugin.AuspicePlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


@SuppressWarnings("unused")
public abstract class NamespaceContainerRegistry<C extends NamespaceContainer> {

    static Map<AuspicePlugin, Map<String, NamespaceContainerRegistry<?>>> a;

    private final Map<Namespace<?>, C> registered = new HashMap<>();

    private final String module;


    public static Pattern ACCEPTED_MODULE_STRING = Pattern.compile("[a-z0-9-]{3,24}");

    public NamespaceContainerRegistry(AuspicePlugin plugin, String module) {
        if (a == null) {
            a = new HashMap<>();
        }
        a.computeIfAbsent(plugin, k -> new HashMap<>());
        this.module = module;
        a.get(plugin).put(module, this);
    }


    public void register(@NotNull NamespaceContainer value) {
        Namespace<?> namespace = value.getNamespace();
        Objects.requireNonNull(namespace, "Cannot register object with null namespace");
        Objects.requireNonNull(value, "Cannot register null object");
        C prev = this.registered.putIfAbsent(namespace, (C) value);
        if (prev != null) {
            throw new IllegalArgumentException(namespace + " was already registered");
        }
    }

    public C getRegistered(@NonNull Namespace<?> namespace) {
        return this.registered.get(namespace);
    }

    public boolean isRegistered(@NonNull Namespace<?> namespace) {
        return this.registered.containsKey(namespace);
    }

    public @NonNull Map<Namespace<?>, C> getRegistry() {
        return Collections.unmodifiableMap(this.registered);
    }

    public String getModule() {
        return module;
    }


    public static Map<String, NamespaceContainerRegistry<?>> getModules(AuspicePlugin plugin) {
        return a.get(plugin);
    }
}
