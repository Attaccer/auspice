package top.auspice.plugin;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PluginRegistry {
    private static final Map<String, AuspicePlugin> names = new HashMap<>();
    private static final Map<String, AuspicePlugin> namespaces = new HashMap<>();

    public static AuspicePlugin getPluginFromName(@NonNull String name) {
        return names.get(name);
    }

    public static AuspicePlugin getPluginFromNamespace(@NonNull String namespace) {
        return namespaces.get(namespace);
    }



    public static void register(AuspicePlugin var0) {
        Objects.requireNonNull(var0, "Addon instance cannot be null");
        if (names.containsKey(var0.getAddonName())) {
            throw new IllegalArgumentException("An plugin with that name already exists: " + var0.getAddonName());
        } else {
            if (namespaces.containsKey(var0.getNamespace())) {
                throw new IllegalArgumentException("An plugin with that namespace already exists: " + var0.getAddonName());
            } else {
                names.put(var0.getAddonName(), var0);
                namespaces.put(var0.getNamespace(), var0);
            }
        }
    }

}
