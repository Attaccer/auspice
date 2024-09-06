package top.auspice.locale.placeholder;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.locale.placeholder.context.PlaceholderTranslationContext;
import top.auspice.locale.placeholder.function.CompiledFunction;

import java.util.function.Function;

public interface EnumPlaceholderTranslator extends PlaceholderTranslator {

    Function<PlaceholderTranslationContext, Object> getTranslator();

    @Nullable
    default Object translate(@NotNull PlaceholderTranslationContext context) {
        return this.getTranslator().apply(context);
    }

    @Nullable
    default CompiledFunction getFunction() {
        Function<?, ?> translator = this.getTranslator();


    }

}
