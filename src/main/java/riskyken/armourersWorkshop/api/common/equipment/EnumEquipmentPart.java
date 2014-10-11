package riskyken.armourersWorkshop.api.common.equipment;



public enum EnumEquipmentPart {
    HEAD(
            1, 0, 1,
            20, 20, 20,
            11, 1, 11,
            EnumBodyPart.HEAD),
    
    CHEST(
            4, 0, 1,
            14, 14, 10,
            11, 13, 6,
            EnumBodyPart.CHEST),
    
    LEFT_ARM(
            1, 0, 11,
            9, 18, 10,
            7, 11, 16,
            EnumBodyPart.LEFT_ARM),
    
    RIGHT_ARM(
            12, 0, 11,
            9, 18, 10,
            15, 11, 16,
            EnumBodyPart.RIGHT_ARM),
    
    LEFT_LEG(
            2, 5, 7,
            8, 9, 8,
            6, 14, 11,
            EnumBodyPart.LEFT_LEG),
    
    RIGHT_LEG(
            12, 5, 7,
            8, 9, 8,
            16, 14, 11,
            EnumBodyPart.RIGHT_LEG),
    
    SKIRT(
            1, 0, 1,
            20, 14, 20,
            11, 14, 11,
            null),
    
    LEFT_FOOT(
            2, 0, 5,
            8, 5, 12,
            6, 14, 11,
            EnumBodyPart.LEFT_LEG),
    
    RIGHT_FOOT(
            12, 0, 5,
            8, 5, 12,
            16, 14, 11,
            EnumBodyPart.RIGHT_LEG),
    
    WEAPON(
            1, 0, 1,
            20, 40, 20,
            11, 21, 11,
            null);
    
    public final int xOffset;
    public final int yOffset;
    public final int zOffset;
    
    public final int xSize;
    public final int ySize;
    public final int zSize;
    
    public final int xOrigin;
    public final int yOrigin;
    public final int zOrigin;
    
    public final EnumBodyPart bodyPart;
    
    EnumEquipmentPart(int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, int xOrigin, int yOrigin, int zOrigin, EnumBodyPart bodyPart) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.zOrigin = zOrigin;
        
        this.bodyPart = bodyPart;
    }
    
    public static EnumEquipmentPart getOrdinal(int id) {
        return EnumEquipmentPart.values()[id];
    }
}
