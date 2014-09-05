package riskyken.armourersWorkshop.common.customarmor;

import net.minecraft.util.StatCollector;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

public enum ArmourType {
    NONE(-1, null),
    HEAD(0, new ArmourPart[] { ArmourPart.HEAD }),
    CHEST(1, new ArmourPart[] { ArmourPart.CHEST, ArmourPart.LEFT_ARM, ArmourPart.RIGHT_ARM }),
    LEGS(2, new ArmourPart[] { ArmourPart.LEFT_LEG, ArmourPart.RIGHT_LEG }),
    SKIRT(2, new ArmourPart[] { ArmourPart.SKIRT }),
    FEET(3, new ArmourPart[] { ArmourPart.LEFT_FOOT, ArmourPart.RIGHT_FOOT });
    
    private final ArmourPart[] parts;
    private final int slotId;
    
    private ArmourType(int slotId, ArmourPart[] parts) {
        this.slotId = slotId;
        this.parts = parts;
    }
    
    public static ArmourType getOrdinal(int id) {
        return ArmourType.values()[id];
    }
    
    public String getLocalizedName() {
        String unlocalizedName = "armourTypes." + LibModInfo.ID.toLowerCase() + ":" + this.name().toLowerCase() + ".name";
        String localizedName = StatCollector.translateToLocal(unlocalizedName);
        if (!unlocalizedName.equals(localizedName)){
            return localizedName;
        }
        return unlocalizedName;
    }
    
    public int getSlotId() {
        return slotId;
    }
    
    public ArmourPart[] getParts() {
        return parts;
    }
    
    
}
