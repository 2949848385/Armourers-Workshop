package moe.plushie.armourers_workshop.common.inventory.slot;

import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.items.ItemSkin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotSkin extends SlotHidable {
    
    private ISkinType skinType;
    
    public SlotSkin(ISkinType skinType, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.skinType = skinType;
        setBackgroundLocation(skinType.getSlotIcon());
    }
    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.getItem() instanceof ItemSkin) {
            if (SkinNBTHelper.stackHasSkinData(stack)) {
                SkinDescriptor skinData = SkinNBTHelper.getSkinDescriptorFromStack(stack);
                if (this.skinType != null && this.skinType == skinData.getIdentifier().getSkinType()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getBackgroundSprite() {
        // TODO Auto-generated method stub
        return new DummySprite("");
    }
    
    @SideOnly(Side.CLIENT)
    public static class DummySprite extends TextureAtlasSprite {

        protected DummySprite(String spriteName) {
            super(spriteName);
        }
        
        @Override
        public float getMaxU() {
            return 1;
        }
        
        @Override
        public float getMaxV() {
            return 1;
        }
        
        @Override
        public float getMinU() {
            return 0;
        }
        
        @Override
        public float getMinV() {
            return 0;
        }
    }
    
    /*
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getBackgroundIconIndex() {
        if (skinType == SkinTypeRegistry.skinSword & getSlotIndex() > 0) {
            if (getSlotIndex() == 1) {
                return ClientProxy.iconSkinPickaxe;
            } else if (getSlotIndex() == 2) {
                return ClientProxy.iconSkinAxe;
            } else if (getSlotIndex() == 3) {
                return ClientProxy.iconSkinShovel;
            } else if (getSlotIndex() == 4) {
                return ClientProxy.iconSkinHoe;
            }
        }
        if (this.skinType != null) {
            return this.skinType.getEmptySlotIcon();
        }
        return super.getBackgroundIconIndex();
    }*/
}
