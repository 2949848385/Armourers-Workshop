package moe.plushie.armourers_workshop.common.skin;

import java.util.ArrayList;
import java.util.BitSet;

import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.data.PlayerPointer;
import moe.plushie.armourers_workshop.common.inventory.IInventorySlotUpdate;
import moe.plushie.armourers_workshop.common.inventory.SkinInventoryContainer;
import moe.plushie.armourers_workshop.common.inventory.WardrobeInventory;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ExPropsPlayerSkinData implements /*IExtendedEntityProperties,*/ IInventorySlotUpdate {

    public static final int MAX_SLOTS_PER_SKIN_TYPE = 8;
    public static final String TAG_EXT_PROP_NAME = "playerCustomEquipmentData";
    private static final String TAG_LAST_XMAS_YEAR = "lastXmasYear";
    private static final ISkinType[] validSkins = {
            SkinTypeRegistry.skinHead,
            SkinTypeRegistry.skinChest,
            SkinTypeRegistry.skinLegs,
            SkinTypeRegistry.skinFeet,
            SkinTypeRegistry.skinSword,
            SkinTypeRegistry.skinBow,
            SkinTypeRegistry.skinWings
            };
    
    private final SkinInventoryContainer wardrobeInventoryContainer;
    private final EntityEquipmentData equipmentData;
    private final EntityPlayer player;
    /** Stores all other wardrobe settings. */
    private PlayerWardrobe equipmentWardrobeData = new PlayerWardrobe(); 
    public int lastXmasYear;
    private boolean allowNetworkUpdates;
    
    public ExPropsPlayerSkinData(EntityPlayer player) {
        allowNetworkUpdates = true;
        this.player = player;
        //An array of all the skins that can be placed in the players wardrobe.

        wardrobeInventoryContainer = new SkinInventoryContainer(this, validSkins);
        equipmentData = new EntityEquipmentData();
    }
    
    public SkinInventoryContainer getWardrobeInventoryContainer() {
        return wardrobeInventoryContainer;
    }
    
    public EntityPlayer getPlayer() {
        return player;
    }
    
    public static final void register(EntityPlayer player) {
        //player.registerExtendedProperties(ExPropsPlayerSkinData.TAG_EXT_PROP_NAME, new ExPropsPlayerSkinData(player));
    }
    
    public static final ExPropsPlayerSkinData get(EntityPlayer player) {
        //return (ExPropsPlayerSkinData) player.getExtendedProperties(TAG_EXT_PROP_NAME);
        return null;
    }
    
    /**
     * @Deprecated Use the {@link #setEquipmentStack(ItemStack, int) setEquipmentStack(ItemStack stack, int index)} method.
     */
    @Deprecated
    public void setEquipmentStack(ItemStack stack) {
        setEquipmentStack(stack, 0);
    }
    
    public void setEquipmentStack(ItemStack stack, int index) {
        SkinDescriptor skinPointer = SkinNBTHelper.getSkinDescriptorFromStack(stack);
        if (skinPointer.getIdentifier().getSkinType() != null) {
            WardrobeInventory wi = wardrobeInventoryContainer.getInventoryForSkinType(skinPointer.getIdentifier().getSkinType());
            if (wi != null) {
                wi.setInventorySlotContents(index, stack);
            }
        }
    }
    
    public boolean setStackInNextFreeSlot(ItemStack stack) {
        SkinDescriptor skinPointer = SkinNBTHelper.getSkinDescriptorFromStack(stack);
        if (skinPointer.getIdentifier().getSkinType() != null) {
            WardrobeInventory wi = wardrobeInventoryContainer.getInventoryForSkinType(skinPointer.getIdentifier().getSkinType());
            if (wi != null) {
                for (int i = 0; i < equipmentWardrobeData.getUnlockedSlotsForSkinType(skinPointer.getIdentifier().getSkinType()); i++) {
                    if (wi.getStackInSlot(i) == null) {
                        wi.setInventorySlotContents(i, stack);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public ItemStack getEquipmentStack(ISkinType skinType, int index) {
        WardrobeInventory wi = wardrobeInventoryContainer.getInventoryForSkinType(skinType);
        if (wi != null) {
            return wi.getStackInSlot(index);
        }
        return null;
    }
    
    public void clearAllEquipmentStacks() {
        ArrayList<ISkinType> skinList = SkinTypeRegistry.INSTANCE.getRegisteredSkinTypes();
        for (int i = 0; i < skinList.size(); i++) {
            ISkinType skinType = skinList.get(i);
            WardrobeInventory wi = wardrobeInventoryContainer.getInventoryForSkinType(skinType);
            if (wi != null) {
                for (int j = 0; j < wi.getSizeInventory(); j++) {
                    wi.setInventorySlotContents(j, null);
                }
            }
        }
    }
    
    public void clearEquipmentStack(ISkinType skinType, int index) {
        WardrobeInventory wi = wardrobeInventoryContainer.getInventoryForSkinType(skinType);
        if (wi != null) {
            wi.setInventorySlotContents(index, null);
        }
    }
    
    public void addCustomEquipment(ISkinType skinType, byte slotId, SkinDescriptor skinPointer) {
        equipmentData.addEquipment(skinType, slotId, skinPointer);
        updateEquipmentDataToPlayersAround();
    }
    
    public void removeCustomEquipment(ISkinType skinType, byte slotId) {
        //ModLogger.log(skinType);
        equipmentData.removeEquipment(skinType, slotId);
        updateEquipmentDataToPlayersAround();
    }
    
    public void updateEquipmentDataToPlayersAround() {
        if (!allowNetworkUpdates) {
            return;
        }
        TargetPoint p = new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 512);
        PlayerPointer playerPointer = new PlayerPointer(player);
        //PacketHandler.networkWrapper.sendToAllAround(new MessageServerSkinInfoUpdate(playerPointer, equipmentData), p);
    }
    
    public EntityEquipmentData getEquipmentData() {
        return equipmentData;
    }
    
    public void armourSlotUpdate(WardrobeInventory inventory, byte slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        
        if (stack == null) {
            removeArmourFromSlot(inventory, slot);
            return;
        } 
        
        if (!stack.hasTagCompound()) {
            return;
        }
        
        if (!SkinNBTHelper.stackHasSkinData(stack)) {
            return;
        }
        
        loadFromItemStack(stack, slot);
    }
    
    private void removeArmourFromSlot(WardrobeInventory inventory, byte slotId) {
        removeCustomEquipment(inventory.getSkinType(), slotId);
    }
    
    public void setSkinColumnCount(ISkinType skinType, int value) {
        if (value > 0 & value <= MAX_SLOTS_PER_SKIN_TYPE) {
            //ModLogger.log(String.format("Setting slot count for %s to %d.", skinType.getRegistryName() ,value));
            equipmentWardrobeData.setUnlockedSlotsForSkinType(skinType, value);
            sendNakedData((EntityPlayerMP) this.player);
        }
    }
    
    public void sendCustomArmourDataToPlayer(EntityPlayerMP targetPlayer) {
        checkAndSendCustomArmourDataTo(targetPlayer);
        sendNakedData(targetPlayer);
    }
    
    public void setSkinInfo(PlayerWardrobe equipmentWardrobeData, boolean sendUpdate) {
        this.equipmentWardrobeData = equipmentWardrobeData;
        if (sendUpdate) {
            sendSkinData();
        }
    }
    
    private void checkAndSendCustomArmourDataTo(EntityPlayerMP targetPlayer) {
        PlayerPointer playerPointer = new PlayerPointer(player);
        //PacketHandler.networkWrapper.sendTo(new MessageServerSkinInfoUpdate(playerPointer, equipmentData), targetPlayer);
    }
    
    private void sendNakedData(EntityPlayerMP targetPlayer) {
        PlayerPointer playerPointer = new PlayerPointer(player);
        //PacketHandler.networkWrapper.sendTo(new MessageServerSkinWardrobeUpdate(playerPointer, this.equipmentWardrobeData), targetPlayer);
    }
    
    private void sendSkinData() {
        TargetPoint p = new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 512);
        PlayerPointer playerPointer = new PlayerPointer(player);
        //PacketHandler.networkWrapper.sendToAllAround(new MessageServerSyncWardrobeCap(playerPointer, this.equipmentWardrobeData), p);
    }
    
    public PlayerWardrobe getEquipmentWardrobeData() {
        return equipmentWardrobeData;
    }
    
    public BitSet getArmourOverride() {
        return equipmentWardrobeData.armourOverride;
    }
    /*
    //@Override
    public void saveNBTData(NBTTagCompound compound) {
        wardrobeInventoryContainer.writeToNBT(compound);
        equipmentWardrobeData.saveNBTData(compound);
        compound.setInteger(TAG_LAST_XMAS_YEAR, this.lastXmasYear);
    }
    
    //@Override
    public void loadNBTData(NBTTagCompound compound) {
        wardrobeInventoryContainer.readFromNBT(compound);
        equipmentWardrobeData.loadNBTData(compound);
        allowNetworkUpdates = false;
        for (int i = 0; i < validSkins.length; i++) {
            WardrobeInventory wi = wardrobeInventoryContainer.getInventoryForSkinType(validSkins[i]);
            if (wi != null) {
                for (int j = 0; j < wi.getSizeInventory(); j++) {
                    armourSlotUpdate(wi, (byte)j);
                }
            }
        }
        allowNetworkUpdates = true;
        if (compound.hasKey(TAG_LAST_XMAS_YEAR)) {
            this.lastXmasYear = compound.getInteger(TAG_LAST_XMAS_YEAR);
        } else {
            this.lastXmasYear = 0;
        }
    }
    */
    private void loadFromItemStack(ItemStack stack, byte slotId) {
        SkinDescriptor skinPointer = SkinNBTHelper.getSkinDescriptorFromStack(stack);
        addCustomEquipment(skinPointer.getIdentifier().getSkinType(), slotId, skinPointer);
    }

    @Override
    public void setInventorySlotContents(IInventory inventory, int slotId, ItemStack stack) {
        if (!player.getEntityWorld().isRemote) {
            if (inventory instanceof WardrobeInventory) {
                armourSlotUpdate((WardrobeInventory) inventory, (byte)slotId);
            }
        }
    }
}
