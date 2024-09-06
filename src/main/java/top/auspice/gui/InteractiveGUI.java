package top.auspice.gui;


import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.ConfigSection;

public abstract class InteractiveGUI {

    @NotNull
    private final ConfigSection d;
    @Nullable
    private Runnable onClose;


}