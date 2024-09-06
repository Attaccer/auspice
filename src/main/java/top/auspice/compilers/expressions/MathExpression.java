package top.auspice.compilers.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.compilers.translators.MathematicalVariableTranslator;

public interface MathExpression extends Expression<MathematicalVariableTranslator, Double> {
    @NotNull
    Double eval(@NotNull MathematicalVariableTranslator translator);

    @Nullable
    default MathExpression nullIfDefault() {
        return this.isDefault() ? null : this;
    }
}

