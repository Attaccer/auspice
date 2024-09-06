package top.auspice.main;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import top.auspice.plugin.AuspicePlugin;

public final class Auspice extends JavaPlugin implements AuspicePlugin {
    private static Auspice instance;

    public Auspice () {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Auspice get() {
        return instance;
    }


    @Override
    public String getNamespace() {
        return "Auspice";
    }

    @Override
    public @NonNull String getAddonName() {
        return "Auspice";
    }
}
