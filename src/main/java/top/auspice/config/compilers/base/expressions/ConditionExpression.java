package top.auspice.config.compilers.base.expressions;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.base.translators.ConditionConfigStringTranslator;

public interface ConditionExpression extends Expression<ConditionConfigStringTranslator, Boolean> {
    @NotNull
    Boolean eval(@NotNull ConditionConfigStringTranslator var1);

    @Nullable
    default ConditionExpression nullIfDefault() {
        return this.isDefault() ? null : this;
    }
}
