package top.auspice.main;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import top.auspice.libs.snakeyaml.Yaml;
import top.auspice.libs.snakeyaml.constructor.Constructor;
import top.auspice.plugin.AuspicePlugin;
import top.auspice.plugin.PluginState;

public final class Auspice extends JavaPlugin implements AuspicePlugin {
    private static Auspice instance;
    private PluginState state;

    public Auspice() {
        instance = this;
        this.state = PluginState.INITIATING;

        if (instance != null || this.state != PluginState.INITIATING) {
            throw new IllegalStateException("Plugin loaded twice");
        }



        this.state = PluginState.INITIATED;
    }

    @Override
    public void onLoad() {
        this.state = PluginState.LOADING;
        registerPlugin();



        this.state = PluginState.LOADED;
    }

    @Override
    public void onEnable() {
        this.state = PluginState.ENABLING;


        this.state = PluginState.ENABLING;
    }

    @Override
    public void onDisable() {

    }

    public static Auspice get() {
        return instance;
    }


    @Pattern(ACCEPTED_NAMESPACE_STRING)
    @NotNull
    @Override
    public String getNamespace() {
        return "Auspice";
    }

    @NotNull
    @Override
    public PluginState getState() {
        return state;
    }

    @Override
    public @NonNull String getAddonName() {
        return "Auspice";
    }
}
