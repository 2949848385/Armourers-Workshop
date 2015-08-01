package riskyken.armourersWorkshop.api.common.skin;

import riskyken.armourersWorkshop.api.common.IPoint3D;

public class Point3D implements IPoint3D {
    
    private final int x;
    private final int y;
    private final int z;
    
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
