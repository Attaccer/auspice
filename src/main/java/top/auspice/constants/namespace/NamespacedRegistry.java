package top.auspice.constants.namespace;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.plugin.AuspicePlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public abstract class NamespacedRegistry<C extends Namespaced<C, R>, R extends NamespacedRegistry<C, R>> {

    static Map<AuspicePlugin, Map<String, NamespacedRegistry<?, ?>>> a;

    private final Map<Namespace<C, R>, C> registered = new HashMap<>();

    private final String module;

    public static final Pattern ACCEPTED_MODULE_STRING = Pattern.compile("[A-Z_]{3,24}");

    protected NamespacedRegistry(AuspicePlugin moduleProvider, @org.intellij.lang.annotations.Pattern("[A-Z_]{3,24}") String module) {
        if (a == null) {
            a = new HashMap<>();
        }
        a.computeIfAbsent(moduleProvider, (k) -> new HashMap<>());
        if (!ACCEPTED_MODULE_STRING.matcher(module).matches()) {
            throw new IllegalArgumentException("Module string doesn't match: '[A-Z_]{3,24}'");
        }
        this.module = module;
        a.get(moduleProvider).put(module, this);
    }


    public void register(@NotNull C value) {
        Namespace<C, R> namespace = value.getNamespace();
        Objects.requireNonNull(namespace, "Cannot register object with null namespace");
        Objects.requireNonNull(value, "Cannot register null object");
        C prev = this.registered.putIfAbsent(namespace, value);
        if (prev != null) {
            throw new IllegalArgumentException(namespace + " was already registered");
        }
    }

    public C getRegistered(@NonNull Namespace<C, R> namespace) {
        return this.registered.get(namespace);
    }

    public boolean isRegistered(@NonNull Namespace<C, R> namespace) {
        return this.registered.containsKey(namespace);
    }

    public @NonNull Map<Namespace<C, R>, C> getRegistry() {
        return Collections.unmodifiableMap(this.registered);
    }

    public String getModule() {
        return module;
    }

    /**
     * @see NamespacedRegistry#getRegisteredNamespaceFromConfigString(String, boolean, boolean)
     */
    @Nullable
    public Namespaced<C, R> getRegisteredFromConfigString(@NotNull String str, boolean checkAbbreviation, boolean checkRepeated) {
        Namespace<C, R> ns = getRegisteredNamespaceFromConfigString(str, checkAbbreviation, checkRepeated);
        if (ns != null) {
            return this.registered.get(ns);
        } else {
            return null;
        }
    }


    /**
     * 从一个命名空间注册器中通过配置文件内的字符串, 寻找对应的命名空间
     *
     * @param str               用于配置文件的命名空间字符串, 比如Auspice:custom; 当形参checkAbbreviation为true时则必须带有':'
     * @param checkAbbreviated 是否检查简写, 比如在一个{@link NamespacedRegistry}实例中只有一个key为custom的{@link Namespace}时可以简写为custom
     * @param checkRepeated     是否检查key重复的命名空间
     * @return 找到的命名空间
     * @throws IllegalArgumentException str中不包含冒号且{@code checkAbbreviated}为true时
     * @throws NamespaceKeyRepeatedException 对应简写字符串能找到多个命名空间时
     */
    @Nullable
    public Namespace<C, R> getRegisteredNamespaceFromConfigString(@NotNull String str, boolean checkAbbreviated, boolean checkRepeated) {
        int sep = str.indexOf(':');
        Namespace<C, R> foundedNs = null;
        if (sep == -1) {                  //若找不到':'
            if (!checkAbbreviated) {     //若不检查简写
                throw new IllegalArgumentException("Namespace string need to have separator ':' when not checking abbreviation");
            } else {
                for (Namespace<C, R> ns : this.registered.keySet()) {
                    if (ns.getKey().equals(Namespace.enumString(str))) {
                        if (checkRepeated && foundedNs != null) {
                            throw new NamespaceKeyRepeatedException("Wrong when get namespace container from abbreviated string: '" + str + "': repeated namespace key");
                        }
                        foundedNs = ns;
                    }
                }
            }

        } else {                          //当找得到':'的时候
            for (Namespace<C, R> ns : this.registered.keySet()) {
                String namespaceStr = str.substring(0, sep);
                String keyString = Namespace.enumString(str.substring(sep + 1));
                if (ns.getNamespace().equals(namespaceStr) && ns.getKey().equals(keyString)) {
                    foundedNs = ns;
                }
            }
        }
        return foundedNs;
    }

    @Nullable
    public Namespaced<C, R> getRegisteredFromString(String str) {
        Namespace<C, R> ns = getRegisteredNamespaceFromString(str);
        if (ns != null) {
            return this.registered.get(ns);
        } else {
            return null;
        }
    }

    /**
     * 从一个命名空间注册器中通过命名空间字符串, 寻找对应的命名空间
     * @param str 命名空间字符串, 比如Auspice:CUSTOM, 它一定要带有':'
     * @return 找到的命名空间
     */
    @Nullable
    public Namespace<C, R> getRegisteredNamespaceFromString(String str) {
        int sep = str.indexOf(':');
        if (sep == -1) {
            throw new IllegalArgumentException("Namespace string must be separated by colon"); //TODO
        }
        String namespaceStr = str.substring(0, sep);
        String keyString = Namespace.enumString(str.substring(sep + 1));
        for (Namespace<C, R> ns : this.registered.keySet()) {
            if (ns.getNamespace().equals(namespaceStr) && ns.getKey().equals(keyString)) {
                return ns;
            }
        }
        return null;
    }

    public static Map<String, NamespacedRegistry<?, ?>> getModules(AuspicePlugin provider) {
        return a.get(provider);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespacedRegistry<?, ?> that = (NamespacedRegistry<?, ?>) o;
        return Objects.equals(registered, that.registered) && Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registered, module);
    }

}
