package top.auspice.locale.message.placeholder.modifier;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.message.placeholders.PlaceholderObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public interface PlaceholderModifier {

    @NotNull
    String getName();
    @NotNull
    Object apply(@NotNull PlaceholderObject var1, @Nullable Object var2);
    boolean isSupported(@NotNull Class<?> var1);
    @NotNull
    Class<?> getOutputType();

    @NotNull
    default Compatibility compareCompatibilityWith(@NotNull PlaceholderModifier modifier) {
        Objects.requireNonNull(modifier);
        Class<?> var2 = this.getOutputType();
        Class<?> var3 = modifier.getOutputType();
        if (var2.equals(var3)) {
            return PlaceholderModifier.Compatibility.COMPATIBLE;
        } else {
            boolean var5 = this.isSupported(var3);
            boolean var4 = modifier.isSupported(var2);
            if (var5 && var4) {
                return PlaceholderModifier.Compatibility.COMPATIBLE;
            } else if (var5) {
                return PlaceholderModifier.Compatibility.AFTER;
            } else {
                return var4 ? PlaceholderModifier.Compatibility.BEFORE : PlaceholderModifier.Compatibility.INCOMPATIBLE;
            }
        }
    }

    static void register(PlaceholderModifier modifier) {
        String name = modifier.getName().toLowerCase(Locale.ENGLISH);
        if (Companion.registered.put(name, modifier) != null) {
            throw new IllegalArgumentException("placeholder modifier already registered: " + modifier);
        }
    }

    static PlaceholderModifier get(String name) {
        return Companion.registered.get(name);
    }

    static Map<String, PlaceholderModifier> getRegistry() {
        return Companion.registered;
    }

    enum Compatibility {
        INCOMPATIBLE,
        COMPATIBLE,
        BEFORE,
        AFTER,

        ;
    }


    static void registerDefaults() {
        register(PlaceholderModifierFancy.INSTANCE);
    }

    class Companion {
        private static final Map<String, PlaceholderModifier> registered = new HashMap<>();

    }

}
