package riskyken.armourersWorkshop.proxies;

import java.util.BitSet;

import net.minecraft.block.Block;
import riskyken.armourersWorkshop.client.render.PlayerSkinInfo;
import riskyken.armourersWorkshop.common.custom.equipment.armour.ArmourType;
import riskyken.armourersWorkshop.common.custom.equipment.data.CustomArmourItemData;

public interface IProxy {

    public abstract void init();
    
    public abstract void initRenderers();
    
    public abstract void postInit();
    
    public abstract void registerKeyBindings();
    
    public abstract void addCustomArmour(String playerName, CustomArmourItemData armourData);
    
    public abstract void removeCustomArmour(String playerName, ArmourType type);
    
    public abstract void removeAllCustomArmourData(String playerName);
    
    public abstract int getPlayerModelCacheSize();

    public abstract void setPlayersNakedData(String playerName, boolean isNaked, int skinColour, int pantsColour, BitSet armourOverride, boolean headOverlay);
    
    public abstract PlayerSkinInfo getPlayersNakedData(String playerName);
    
    public abstract int getRenderType(Block block);
    
    public abstract void receivedEquipmentData(CustomArmourItemData equipmentData);
}
