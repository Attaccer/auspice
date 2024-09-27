package top.auspice.yaml.transformars.std;

import org.bukkit.OfflinePlayer;
import top.auspice.utils.PlayerUtils;
import top.auspice.yaml.transformars.base.Transformer;

public class OfflinePlayerTransformer implements Transformer<OfflinePlayer> {
    @Override
    public OfflinePlayer transform(String s) {
        return PlayerUtils.getOfflinePlayer(s);
    }

    @Override
    public Class<OfflinePlayer> getType() {
        return OfflinePlayer.class;
    }


}
