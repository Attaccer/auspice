package top.auspice.locale.message.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import top.auspice.locale.message.placeholder.target.PlaceholderTargets;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderTranslationContext {
    public final PlaceholderTargets targets;

    private final Map<String, Object> functionsInfo = new HashMap<>();

    String placeholderString;

    public PlaceholderTranslationContext() {
        this.targets = new PlaceholderTargets();
    }

    public PlaceholderTargets getTargets() {
        return targets;
    }


    @Nullable
    public Player getPlayer() {
        return (Player) targets.getPrimary(Player.class);
    }

    @Nullable
    public Player getOtherPlayer() {
        return (Player) targets.getSecondary(Player.class);
    }


    public OfflinePlayer getOfflinePlayer() {
        return (OfflinePlayer) targets.getPrimary(OfflinePlayer.class);
    }

    public OfflinePlayer getOtherOfflinePlayer() {
        return (OfflinePlayer) targets.getSecondary(OfflinePlayer.class);
    }


    public Map<String, Object> getFunctionsInfo() {
        return functionsInfo;
    }

    public String getPlaceholderString() {
        return placeholderString;
    }

    public void setPlaceholderString(String placeholderString) {
        this.placeholderString = placeholderString;
    }


}
