package riskyken.armourersWorkshop.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.common.items.ItemEquipmentSkin;
import riskyken.armourersWorkshop.common.skin.data.SkinPointer;
import riskyken.armourersWorkshop.utils.EquipmentNBTHelper;

public class SlotEquipmentSkin extends SlotHidable {
    
    private ISkinType skinType;
    
    public SlotEquipmentSkin(ISkinType skinType, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.skinType = skinType;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.getItem() instanceof ItemEquipmentSkin) {
            if (EquipmentNBTHelper.stackHasSkinData(stack)) {
                SkinPointer skinData = EquipmentNBTHelper.getSkinPointerFromStack(stack);
                if (this.skinType != null && this.skinType == skinData.skinType) {
                    return true;
                }
            }
        }
        return false;
    }
}
