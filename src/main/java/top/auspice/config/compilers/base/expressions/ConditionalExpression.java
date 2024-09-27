package top.auspice.config.compilers.base.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.base.translators.ConditionalVariableTranslator;

public interface ConditionalExpression extends Expression<ConditionalVariableTranslator, Boolean> {
    @NotNull
    Boolean eval(@NotNull ConditionalVariableTranslator var1);

    @Nullable
    default ConditionalExpression nullIfDefault() {
        return this.isDefault() ? null : this;
    }
}
