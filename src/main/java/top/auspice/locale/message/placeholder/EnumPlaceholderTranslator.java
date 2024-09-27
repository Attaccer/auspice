package top.auspice.locale.message.placeholder;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public interface EnumPlaceholderTranslator extends PlaceholderTranslator {


    @Nullable
    default Object translate(@NotNull PlaceholderTranslationContext context) {
        return this.getFunction().apply(context);
    }


}
