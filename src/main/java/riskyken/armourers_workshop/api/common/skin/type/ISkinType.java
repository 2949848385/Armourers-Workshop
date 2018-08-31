package riskyken.armourers_workshop.api.common.skin.type;

import java.util.ArrayList;

import riskyken.armourers_workshop.api.common.skin.data.ISkinProperty;

/**
 * 
 * @author RiskyKen
 *
 */
public interface ISkinType {

    public ArrayList<ISkinPartType> getSkinParts();
    
    /**
     * Gets the name this skin will be registered with.
     * Armourer's Workshop uses the format armourers:skinName.
     * Example armourers:head is the registry name of
     * Armourer's Workshop head armour skin.
     * @return registryName
     */
    public String getRegistryName();
    
    /**
     * This only exists for backwards compatibility with old world saves.
     * Just return getRegistryName().
     * @return name
     */
    public String getName();
    
    /*
    @SideOnly(Side.CLIENT)
    public void registerIcon(IIconRegister register);
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon();
    
    @SideOnly(Side.CLIENT)
    public IIcon getEmptySlotIcon();
    */
    /**
     * Should the show skin overlay check box be shown in the armourer and mini armourer.
     * @return
     */
    public boolean showSkinOverlayCheckbox();
    
    /**
     * Should the helper check box be shown in the armourer and mini armourer.
     * @return
     */
    public boolean showHelperCheckbox();
    
    /**
     * If this skin is for vanilla armour return the slot id here, otherwise return -1.
     * @return slotId
     */
    public int getVanillaArmourSlotId();
    
    /**
     * Should this skin be hidden from the user?
     * @return Is hidden?
     */
    public boolean isHidden();
    
    /**
     * Is this skin enabled?
     * @return Is enabled?
     */
    public boolean enabled();
    
    public ArrayList<ISkinProperty<?>> getProperties();
}
