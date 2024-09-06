package top.auspice.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class GUIOption {
    protected final String name;

    private ItemStack a;

    protected Map<ClickType, Runnable> runnables;

    public GUIOption(String name) {
        this.name = (String) Objects.requireNonNull(name, "GUI option name is null");
    }

    public String getName() {
        return this.name;
    }


}
