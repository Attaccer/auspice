package top.auspice.constants.location;

import org.jetbrains.annotations.NotNull;

public final class SimpleChunkLocation implements Block2D, WorldNameContainer {
    private final String world;
    private final int x;
    private final int z;

    public SimpleChunkLocation(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @NotNull
    @Override
    public String getWorldName() {
        return this.world;
    }
    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getZ() {
        return z;
    }


}
