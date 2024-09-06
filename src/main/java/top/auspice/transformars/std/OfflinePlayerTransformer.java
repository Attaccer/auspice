package top.auspice.transformars.std;

import org.bukkit.OfflinePlayer;
import top.auspice.transformars.base.Transformer;

public class OfflinePlayerTransformer implements Transformer<OfflinePlayer> {
    @Override
    public OfflinePlayer transform(String s) {
        return null;
    }

    @Override
    public Class<OfflinePlayer> getType() {
        return OfflinePlayer.class;
    }


}
