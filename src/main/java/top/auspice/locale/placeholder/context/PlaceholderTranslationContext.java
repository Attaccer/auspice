package top.auspice.locale.placeholder.context;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import top.auspice.locale.placeholder.target.PlaceholderTargets;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderTranslationContext {
    public final PlaceholderTargets targets;

    private final Map<String, Map<String, Object>> functionsInfo = new HashMap<>();

    String placeholderString;

    public PlaceholderTranslationContext() {
        this.targets = new PlaceholderTargets();
    }

    public PlaceholderTargets getTargets() {
        return targets;
    }


    @Nullable
    public Player getPlayer() {
        return (Player) targets.get(Player.class);
    }

    @Nullable
    public Player getOtherPlayer() {
        return (Player) targets.getOther(Player.class);
    }


    public OfflinePlayer getOfflinePlayer() {
        return (OfflinePlayer) targets.get(OfflinePlayer.class);
    }

    public OfflinePlayer getOtherOfflinePlayer() {
        return (OfflinePlayer) targets.getOther(OfflinePlayer.class);
    }


    public Map<String, Map<String, Object>> getFunctionsInfo() {
        return functionsInfo;
    }

    public String getPlaceholderString() {
        return placeholderString;
    }

    public void setPlaceholderString(String placeholderString) {
        this.placeholderString = placeholderString;
    }


}
