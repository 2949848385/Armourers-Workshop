package moe.plushie.armourers_workshop.common.crafting.recipe;

import moe.plushie.armourers_workshop.common.items.ModItems;
import moe.plushie.armourers_workshop.common.skin.data.SkinDye;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeSkinCopy extends RecipeItemSkinning {

    public RecipeSkinCopy() {
        super(null);
    }

    @Override
    public boolean matches(IInventory inventory) {
        return getCraftingResult(inventory) != null;
    }
    
    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        ItemStack skinStack = null;
        ItemStack blackStack = null;
        
        for (int slotId = 0; slotId < inventory.getSizeInventory(); slotId++) {
            ItemStack stack = inventory.getStackInSlot(slotId);
            if (stack != null) {
                Item item = stack.getItem();
                
                if (item == ModItems.Skin && SkinNBTHelper.stackHasSkinData(stack)) {
                    if (skinStack != null) {
                        return null;
                    }
                    skinStack = stack;
                } else if (item == ModItems.SkinTemplate & !SkinNBTHelper.stackHasSkinData(stack)) {
                    if (blackStack != null) {
                        return null;
                    }
                    blackStack = stack;
                } else {
                    return null;
                }
                
            }
        }
        
        if (skinStack != null && blackStack != null) {
            ItemStack returnStack = new ItemStack(ModItems.Skin, 1);
            SkinDescriptor skinData = SkinNBTHelper.getSkinDescriptorFromStack(skinStack);
            SkinNBTHelper.addSkinDataToStack(returnStack, skinData.getIdentifier(), false, new SkinDye(skinData.getSkinDye()));
            return returnStack;
        }
        return null;
    }
    
    @Override
    public void onCraft(IInventory inventory) {
        for (int slotId = 0; slotId < inventory.getSizeInventory(); slotId++) {
            ItemStack stack = inventory.getStackInSlot(slotId);
            Item item = stack.getItem();
            if (item == ModItems.SkinTemplate & !SkinNBTHelper.stackHasSkinData(stack)) {
                inventory.decrStackSize(slotId, 1);
            }
        }
    }
}
