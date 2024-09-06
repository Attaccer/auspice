package top.auspice.compilers.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.compilers.translators.ConditionalVariableTranslator;

public interface ConditionalExpression extends Expression<ConditionalVariableTranslator, Boolean> {
    @NotNull
    Boolean eval(@NotNull ConditionalVariableTranslator var1);

    @Nullable
    default ConditionalExpression nullIfDefault() {
        return this.isDefault() ? null : this;
    }
}
