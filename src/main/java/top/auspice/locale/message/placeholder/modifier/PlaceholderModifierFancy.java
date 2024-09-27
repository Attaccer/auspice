package top.auspice.locale.message.placeholder.modifier;

import kotlin.jvm.internal.Intrinsics;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.message.placeholders.PlaceholderObject;
import top.auspice.utils.string.Strings;

import java.util.Objects;

public final class PlaceholderModifierFancy implements PlaceholderModifier {
    @NotNull
    public static final PlaceholderModifierFancy INSTANCE = new PlaceholderModifierFancy();
    @NotNull
    private static final String a = "fancy";

    private PlaceholderModifierFancy() {
    }

    @NotNull
    public String getName() {
        return a;
    }

    @NotNull
    public Object apply(@NotNull PlaceholderObject var1, @Nullable Object var2) {
        Objects.requireNonNull(var1);
        if (!(var2 instanceof Number)) {
            throw PlaceholderModifierException.unsupported(this, var1, var2);
        } else {
            String var10000 = Strings.toFancyNumber(((Number)var2).doubleValue());
            Objects.requireNonNull(var10000);
            return var10000;
        }
    }

    public boolean isSupported(@NotNull Class<?> var1) {
        Intrinsics.checkNotNullParameter(var1, "");
        return Number.class.isAssignableFrom(var1);
    }

    @NotNull
    public Class<?> getOutputType() {
        return String.class;
    }
}

