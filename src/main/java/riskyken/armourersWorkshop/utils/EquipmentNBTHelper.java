package riskyken.armourersWorkshop.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import riskyken.armourersWorkshop.api.common.equipment.skin.IEquipmentSkinType;
import riskyken.armourersWorkshop.common.equipment.data.EquipmentSkinTypeData;
import riskyken.armourersWorkshop.common.equipment.skin.SkinTypeRegistry;
import riskyken.armourersWorkshop.common.items.ModItems;

public class EquipmentNBTHelper {
    
    private static final String TAG_OLD_SKIN_DATA = "armourData";
    public static final String TAG_OLD_SKIN_ID = "equpmentId";
    
    private static final String TAG_SKIN_DATA = "armourersWorkshop";
    private static final String TAG_SKIN_TYPE = "skinType";
    private static final String TAG_SKIN_ID = "skinId";
    private static final String TAG_SKIN_LOCK = "lock";
    
    public static boolean stackHasSkinData(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        
        if (!stack.hasTagCompound()) {
            return false;
        }
        
        NBTTagCompound itemCompound = stack.getTagCompound();
        if (!itemCompound.hasKey(TAG_SKIN_DATA)) {
            return false;
        }
        
        return true;
    }
    
    public static void removeSkinDataFromStack(ItemStack stack, boolean overrideLock) {
        if (!stackHasSkinData(stack)) {
            return;
        }
        
        SkinNBTData skinData = getSkinNBTDataFromStack(stack);
        if (skinData.lockSkin) {
            if (!overrideLock) {
                return;
            }
        }
        
        NBTTagCompound itemCompound = stack.getTagCompound();
        if (itemCompound.hasKey(TAG_SKIN_DATA)) {
            itemCompound.removeTag(TAG_SKIN_DATA);
        }
    }
    
    public static SkinNBTData getSkinNBTDataFromStack(ItemStack stack) {
        if (!stackHasSkinData(stack)) {
            return null;
        }
        
        SkinNBTData skinData = new SkinNBTData();
        skinData.readFromCompound(stack.getTagCompound());
        
        return skinData;
    }
    
    public static IEquipmentSkinType getSkinTypeFromStack(ItemStack stack) {
        if (!stackHasSkinData(stack)) {
            return null;
        }
        
        SkinNBTData skinData = new SkinNBTData();
        skinData.readFromCompound(stack.getTagCompound());
        
        return skinData.skinType;
    }
    
    public static int getSkinIdFromStack(ItemStack stack) {
        if (!stackHasSkinData(stack)) {
            return -1;
        }
        
        SkinNBTData skinData = new SkinNBTData();
        skinData.readFromCompound(stack.getTagCompound());
        
        return skinData.skinId;
    }
    
    public static boolean isSkinLockedOnStack(ItemStack stack) {
        if (!stackHasSkinData(stack)) {
            return false;
        }
        
        SkinNBTData skinData = new SkinNBTData();
        skinData.readFromCompound(stack.getTagCompound());
        
        return skinData.lockSkin;
    }
    
    public static void addSkinDataToStack(ItemStack stack, IEquipmentSkinType skinType, int skinId, boolean lockSkin) {
        SkinNBTData skinData = new SkinNBTData(skinType, skinId, lockSkin);
        addSkinDataToStack(stack, skinData);
    }
    
    public static void addSkinDataToStack(ItemStack stack, SkinNBTData skinData) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        skinData.writeToCompound(stack.getTagCompound());
    }
    
    public static boolean stackHasLegacySkinData(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        
        if (!stack.hasTagCompound()) {
            return false;
        }
        
        NBTTagCompound itemCompound = stack.getTagCompound();
        if (!itemCompound.hasKey(TAG_OLD_SKIN_DATA)) {
            return false;
        }
        
        NBTTagCompound skinDataCompound = itemCompound.getCompoundTag(TAG_OLD_SKIN_DATA);
        if (!skinDataCompound.hasKey(TAG_OLD_SKIN_ID)) {
            return false;
        }
        
        return true;
    }
    
    public static int getLegacyIdFromStack(ItemStack stack) {
        if (stack == null) {
            return -1;
        }
        if (!stack.hasTagCompound()) {
            return -1;
        }
        
        NBTTagCompound itemCompound = stack.getTagCompound();
        if (!itemCompound.hasKey(TAG_OLD_SKIN_DATA)) {
            return -1;
        }
        
        NBTTagCompound skinDataCompound = itemCompound.getCompoundTag(TAG_OLD_SKIN_DATA);
        if (!skinDataCompound.hasKey(TAG_OLD_SKIN_ID)) {
            return -1;
        }
        
        return skinDataCompound.getInteger(TAG_OLD_SKIN_ID);
    }
    
    public static ItemStack makeEquipmentSkinStack(EquipmentSkinTypeData equipmentItemData) {
        ItemStack stack = new ItemStack(ModItems.equipmentSkin, 1);
        stack.setTagCompound(new NBTTagCompound());
        addSkinDataToStack(stack, equipmentItemData.getSkinType(), equipmentItemData.hashCode(), false);
        return stack;
    }
    
    public static ItemStack makeArmouerContainerStack(EquipmentSkinTypeData equipmentItemData) {
        ItemStack stack = new ItemStack(ModItems.armourContainer[equipmentItemData.getSkinType().getVanillaArmourSlotId()], 1);
        stack.setTagCompound(new NBTTagCompound());
        addSkinDataToStack(stack, equipmentItemData.getSkinType(), equipmentItemData.hashCode(), false);
        return stack;
    }
    
    public static void addRenderIdToStack(ItemStack stack, IEquipmentSkinType skinType, int skinId) {
        if (stackHasSkinData(stack)) {
            SkinNBTData skinData = getSkinNBTDataFromStack(stack);
            if (skinData.skinId != skinId & !skinData.lockSkin) {
                addSkinDataToStack(stack, skinType, skinId, false);
            }
        } else {
            addSkinDataToStack(stack, skinType, skinId, false);
        }
    }
    
    public static void removeRenderIdFromStack(ItemStack stack) {
        removeSkinDataFromStack(stack, false);
    }
    
    public static class SkinNBTData {
        public IEquipmentSkinType skinType;
        public int skinId;
        public boolean lockSkin;
        
        public SkinNBTData() {
        }
        
        public SkinNBTData(IEquipmentSkinType skinType, int skinId, boolean lockSkin) {
            this.skinType = skinType;
            this.skinId = skinId;
            this.lockSkin = lockSkin;
        }
        
        public void readFromCompound(NBTTagCompound compound) {
            NBTTagCompound skinDataCompound = compound.getCompoundTag(TAG_SKIN_DATA);
            this.skinType = SkinTypeRegistry.INSTANCE.getSkinTypeFromRegistryName(skinDataCompound.getString(TAG_SKIN_TYPE));
            this.skinId = skinDataCompound.getInteger(TAG_SKIN_ID);
            this.lockSkin = skinDataCompound.getBoolean(TAG_SKIN_LOCK);
        }

        public void writeToCompound(NBTTagCompound compound) {
            NBTTagCompound skinDataCompound = new NBTTagCompound();
            skinDataCompound.setString(TAG_SKIN_TYPE, this.skinType.getRegistryName());
            skinDataCompound.setInteger(TAG_SKIN_ID, this.skinId);
            skinDataCompound.setBoolean(TAG_SKIN_LOCK, this.lockSkin);
            compound.setTag(TAG_SKIN_DATA, skinDataCompound);
        }
    }
}
