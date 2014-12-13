package riskyken.armourersWorkshop.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.api.common.equipment.EnumEquipmentType;
import riskyken.armourersWorkshop.api.common.lib.LibCommonTags;
import riskyken.armourersWorkshop.client.equipment.ClientEquipmentModelCache;
import riskyken.armourersWorkshop.common.handler.EquipmentDataHandler;

public class RenderItemEquipmentSkin implements IItemRenderer {

    private final RenderItem renderItem;
    private final Minecraft mc;

    public RenderItemEquipmentSkin() {
        renderItem = (RenderItem) RenderManager.instance.entityRenderMap.get(EntityItem.class);
        mc = Minecraft.getMinecraft();
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return canRenderModel(stack);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (canRenderModel(stack)) {
            GL11.glPushMatrix();
            GL11.glScalef(-1F, -1F, 1F);
            float scale = 1.2F;
            GL11.glScalef(scale, scale, scale);
            GL11.glRotatef(180, 0, 1, 0);
            
            EnumEquipmentType equipmentType = EquipmentDataHandler.INSTANCE.getEquipmentTypeFromStack(stack);
            if (equipmentType == EnumEquipmentType.SWORD) {
                GL11.glScalef(0.7F, 0.7F, 0.7F);
            }
            
            switch (type) {
            case EQUIPPED:
                GL11.glTranslatef(0.6F, -0.5F, -0.5F);
                GL11.glRotatef(180, 0, 1, 0);
                break;
            case ENTITY:
                GL11.glTranslatef(0F, -0.3F, 0F);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.5F, -0.7F, -0.5F);
                GL11.glRotatef(90, 0, 1, 0);
                break;
            default:
                break;
            }
            EquipmentItemRenderCache.renderItemAsArmourModel(stack);
            GL11.glPopMatrix();
            
        } else {
            renderNomalIcon(stack);
        }
    }
    
    private boolean canRenderModel(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return false;
        }
        NBTTagCompound armourNBT = stack.getTagCompound().getCompoundTag(LibCommonTags.TAG_ARMOUR_DATA);
        if (armourNBT == null) { return false; }
        if (!armourNBT.hasKey(LibCommonTags.TAG_EQUIPMENT_ID)) { return false; }
        int equipmentId = armourNBT.getInteger(LibCommonTags.TAG_EQUIPMENT_ID);
        if (ClientEquipmentModelCache.INSTANCE.isEquipmentInCache(equipmentId)) {
            return true;
        } else {
            ClientEquipmentModelCache.INSTANCE.requestEquipmentDataFromServer(equipmentId);
            return false;
        }
    }
    
    private void renderNomalIcon(ItemStack stack) {
        IIcon icon = stack.getItem().getIcon(stack, 0);
        renderItem.renderIcon(0, 0, icon, icon.getIconWidth(), icon.getIconHeight());
        icon = stack.getItem().getIcon(stack, 1);
        renderItem.renderIcon(0, 0, icon, icon.getIconWidth(), icon.getIconHeight());
    }
}
