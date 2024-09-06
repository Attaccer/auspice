package top.auspice.compilers.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.compilers.translators.VariableTranslator;


public interface Expression<VAR extends VariableTranslator<?>, OUT> {
    OUT eval(@NotNull VAR var1);

    boolean isDefault();

    @Nullable
    Expression<VAR, OUT> nullIfDefault();

    @Nullable
    String getOriginalString();

    @NotNull
    String asString(boolean var1);
}
