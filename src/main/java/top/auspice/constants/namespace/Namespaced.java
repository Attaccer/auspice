package top.auspice.constants.namespace;

/**
 * 命名空间容器
 */
public interface Namespaced<C extends Namespaced<C, R>, R extends NamespacedRegistry<C, R>> {
    Namespace<C, R> getNamespace();

}
