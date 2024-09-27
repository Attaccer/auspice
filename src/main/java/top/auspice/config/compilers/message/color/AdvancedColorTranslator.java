package top.auspice.config.compilers.message.color;

import top.auspice.config.compilers.message.pieces.MessagePiece;
import top.auspice.constants.namespace.Namespace;
import top.auspice.constants.namespace.Namespaced;

public abstract class AdvancedColorTranslator implements Namespaced<AdvancedColorTranslator, AdvancedColorRegistry> {
    private final Namespace<AdvancedColorTranslator, AdvancedColorRegistry> namespace;
    private final boolean scoped;
    @Override
    public final Namespace<AdvancedColorTranslator, AdvancedColorRegistry> getNamespace() {
        return this.namespace;
    }

    public AdvancedColorTranslator(Namespace<AdvancedColorTranslator, AdvancedColorRegistry> namespace, boolean scoped) {
        if (namespace.getRegistry() != AdvancedColorRegistry.INSTANCE) {
            throw new IllegalArgumentException("Wrong namespace container");
        }
        this.namespace = namespace;
        this.scoped = scoped;
    }

    public final boolean isScoped() {
        return this.scoped;
    }


    public abstract MessagePiece[] build(String text, String... options);




}
