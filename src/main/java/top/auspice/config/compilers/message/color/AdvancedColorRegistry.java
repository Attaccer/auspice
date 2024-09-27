package top.auspice.config.compilers.message.color;

import top.auspice.constants.namespace.NamespacedRegistry;
import top.auspice.main.Auspice;

public final class AdvancedColorRegistry extends NamespacedRegistry<AdvancedColorTranslator, AdvancedColorRegistry> {
    public static final AdvancedColorRegistry INSTANCE = new AdvancedColorRegistry();
    private AdvancedColorRegistry() {
        super(Auspice.get(), "ADVANCED_COLOR");
    }
    public static AdvancedColorRegistry get() {
        return INSTANCE;
    }

}
