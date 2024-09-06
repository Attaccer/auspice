package top.auspice.locale.placeholder;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import top.auspice.locale.placeholder.context.PlaceholderTranslationContext;
import top.auspice.locale.placeholder.function.CompiledFunction;
import top.auspice.plugin.AuspicePlugin;

public interface PlaceholderTranslator {

    @Pattern("[A-Za-z-_]{3,100}") String getName();

    Object getDefault();
    Object getConfiguredDefaultValue();
    void setConfiguredDefaultValue(@Nullable Object var1);
    Object translate(@NotNull PlaceholderTranslationContext var1);

    CompiledFunction getFunction();

    AuspicePlugin getNamespace();


}
