package riskyken.armourersWorkshop.proxies;

import net.minecraft.block.Block;
import riskyken.armourersWorkshop.common.data.PlayerPointer;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerClientCommand.CommandType;
import riskyken.armourersWorkshop.common.skin.EntityEquipmentData;
import riskyken.armourersWorkshop.common.skin.data.Skin;

public class CommonProxy {
    
    public void preInit() {
        
    }
    
    public void initRenderers() {
        
    }
    
    public void init() {
        
    }
    
    public void postInit() {
        
    }
    
    public void registerKeyBindings() {
        
    }
    
    public void addEquipmentData(PlayerPointer playerPointer, EntityEquipmentData equipmentData) {
        
    }
    
    public int getPlayerModelCacheSize() {
        return 0;
    }
    
    public int getRenderType(Block block) {
        return 0;
    }
    
    public void receivedEquipmentData(Skin equipmentData) {
        
    }
    
    public void receivedCommandFromSever(CommandType command) {
        
    }
    
    public void receivedEquipmentData(EntityEquipmentData equipmentData, int entityId) {
        
    }
    
    public void receivedSkinFromLibrary(String fileName, Skin skin) {
        
    }
}
