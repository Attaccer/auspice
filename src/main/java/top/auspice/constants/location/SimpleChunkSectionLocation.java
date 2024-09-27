package top.auspice.constants.location;

import org.jetbrains.annotations.NotNull;

public final class SimpleChunkSectionLocation implements Block3D, WorldNameContainer {
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public SimpleChunkSectionLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SimpleChunkLocation toSimpleChunkLocation() {
        return new SimpleChunkLocation(this.world, this.x, this.z);
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
    public int getY() {
        return y;
    }
    @Override
    public int getZ() {
        return z;
    }
}
