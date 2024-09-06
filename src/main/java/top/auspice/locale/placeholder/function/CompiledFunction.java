package top.auspice.locale.placeholder.function;

import org.jetbrains.annotations.NotNull;
import top.auspice.locale.placeholder.context.PlaceholderTranslationContext;

import java.util.function.Function;

public class CompiledFunction {

    private final Function<PlaceholderTranslationContext, Object> function;

    public CompiledFunction(@NotNull Function<PlaceholderTranslationContext, Object> function) {
        this.function = function;
    }

    public Function<PlaceholderTranslationContext, Object> getFunction() {
        return this.function;
    }


}
