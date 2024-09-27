package top.auspice.config.compilers.message.color;

import net.minecraft.util.ColorUtil;
import top.auspice.config.compilers.message.pieces.MessagePiece;
import top.auspice.constants.namespace.Namespace;
import top.auspice.utils.ColorUtils;

import java.util.function.BiFunction;

public final class StandardAdvancedColorTranslator extends AdvancedColorTranslator {

    public static final StandardAdvancedColorTranslator UNIFORM_GRADIENT = a("UNIFORM_GRADIENT", true, (text, options) -> {
        if (options.length == 2) {
            return ColorUtils.gradient()
        } else {
            return text;
        }
    });
    public static final StandardAdvancedColorTranslator

    public static void init() {

    }

    private final BiFunction<String, String[], MessagePiece[]> translator;
    private static StandardAdvancedColorTranslator a(String key, boolean scoped, BiFunction<String, String[], MessagePiece[]> translator) {
        StandardAdvancedColorTranslator c = new StandardAdvancedColorTranslator(new Namespace<>(AdvancedColorRegistry.INSTANCE, "Auspice", key), scoped, translator);
        AdvancedColorRegistry.INSTANCE.register(c);
        return c;
    }

    public StandardAdvancedColorTranslator(Namespace<AdvancedColorTranslator, AdvancedColorRegistry> namespace, boolean scoped, BiFunction<String, String[], MessagePiece[]> translator) {
        super(namespace, scoped);
        this.translator = translator;
    }

    @Override
    public MessagePiece[] build(String text, String... options) {
        return this.translator.apply(text, options);
    }
}
