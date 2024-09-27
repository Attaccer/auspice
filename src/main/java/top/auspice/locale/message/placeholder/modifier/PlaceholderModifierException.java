package top.auspice.locale.message.placeholder.modifier;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.message.placeholders.PlaceholderObject;

public final class PlaceholderModifierException extends RuntimeException {


    public PlaceholderModifierException(@NotNull String var1) {
        super(var1);
    }

    @Contract("_, _, _, -> fail")
    public static PlaceholderModifierException unsupported(@NotNull PlaceholderModifier modifier, @NotNull PlaceholderObject placeholder, @Nullable Object object) {
        return new PlaceholderModifierException("Unsupported placeholder value for modifier '" + modifier.getName() + "' with value (" + (object != null ? object.getClass() : null) + ") " + object + " for placeholder: " + placeholder);
    }


}