package riskyken.armourersWorkshop.api.common.equipment.skin;

import java.util.ArrayList;

/**
 * Skin type registry is used to register new ISkinType's
 * and get register ISkinType's and ISkinPart's.
 * 
 * @author RiskyKen
 *
 */
public interface ISkinTypeRegistry {
    
    /**
     * Register a new skin type.
     * @param skinType
     */
    public void registerSkin(ISkinType skinType) ;
    
    public ISkinType getSkinTypeFromRegistryName(String registryName);
    
    public ISkinPart getSkinPartFromRegistryName(String registryName);
    
    public ArrayList<ISkinType> getRegisteredSkinTypes();
    
    public int getNumberOfSkinRegistered();
}
