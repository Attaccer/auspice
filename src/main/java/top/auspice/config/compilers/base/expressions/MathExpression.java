package top.auspice.config.compilers.base.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.base.translators.MathConfigStringTranslator;

public interface MathExpression extends Expression<MathConfigStringTranslator, Double> {
    @NotNull
    Double eval(@NotNull MathConfigStringTranslator translator);

    @Nullable
    default MathExpression nullIfDefault() {
        return this.isDefault() ? null : this;
    }
}

