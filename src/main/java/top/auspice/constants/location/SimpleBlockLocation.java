package top.auspice.constants.location;

import org.jetbrains.annotations.NotNull;

public final class SimpleBlockLocation implements Block3D, WorldNameContainer {
    private final String world;
    private final int x;
    private final int y;
    private final int z;


    public SimpleBlockLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static SimpleBlockLocation of(String world, int x, int y, int z) {
        return new SimpleBlockLocation(world, x, y, z);
    }

    @NotNull
    @Override
    public String getWorldName() {
        return this.world;
    }
    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }
}
