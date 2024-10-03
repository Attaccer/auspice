package top.auspice.config.compilers.base.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.base.translators.ConfigStringTranslator;


public interface Expression<VAR extends ConfigStringTranslator<?>, OUT> {
    OUT eval(@NotNull VAR var1);

    boolean isDefault();

    @Nullable
    Expression<VAR, OUT> nullIfDefault();

    @Nullable
    String getOriginalString();

    @NotNull
    String asString(boolean var1);
}
