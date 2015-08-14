package riskyken.armourersWorkshop.client.handler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import riskyken.armourersWorkshop.api.client.render.ISkinRenderHandler;
import riskyken.armourersWorkshop.api.common.skin.data.ISkin;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinPointer;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.client.model.ClientModelCache;
import riskyken.armourersWorkshop.client.model.armourer.ModelHand;
import riskyken.armourersWorkshop.client.render.EquipmentModelRenderer;
import riskyken.armourersWorkshop.client.render.EquipmentPartRenderer;
import riskyken.armourersWorkshop.common.data.PlayerPointer;
import riskyken.armourersWorkshop.common.skin.EquipmentWardrobeData;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.common.skin.data.SkinPointer;
import riskyken.armourersWorkshop.proxies.ClientProxy;
import riskyken.armourersWorkshop.utils.EquipmentNBTHelper;

public class EquipmentRenderHandler implements ISkinRenderHandler {

    public static final EquipmentRenderHandler INSTANCE = new EquipmentRenderHandler();
    
    @Override
    public boolean renderSkinWithHelper(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return EquipmentModelRenderer.INSTANCE.renderEquipmentPartFromStack(stack, null);
    }

    @Override
    public boolean renderSkinWithHelper(ItemStack stack, ModelBiped modelBiped) {
        if (stack == null) {
            return false;
        }
        return EquipmentModelRenderer.INSTANCE.renderEquipmentPartFromStack(stack, modelBiped);
    }

    @Override
    public boolean renderSkinWithHelper(ItemStack stack, float limb1, float limb2, float limb3, float headY, float headX) {
        if (stack == null) {
            return false;
        }
        ISkinPointer skinPointer = EquipmentNBTHelper.getSkinPointerFromStack(stack);
        if (skinPointer == null) {
            return false;
        }
        return renderSkinWithHelper(skinPointer, limb1, limb2, limb3, headY, headX);
    }
    
    @Override
    public boolean renderSkinWithHelper(ISkinPointer skinPointer) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean renderSkinWithHelper(ISkinPointer skinPointer,
            ModelBiped modelBiped) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean renderSkinWithHelper(ISkinPointer skinPointer, float limb1, float limb2, float limb3, float headY, float headX) {
        if (skinPointer == null) {
            return false;
        }
        return EquipmentModelRenderer.INSTANCE.renderEquipmentPartFromSkinPointer(skinPointer, limb1, limb2, limb3, headY, headX);
    }
    
    @Override
    public boolean renderSkin(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        ISkinPointer skinPointer = EquipmentNBTHelper.getSkinPointerFromStack(stack);
        if (skinPointer != null) {
            return renderSkin(skinPointer);
        }
        return false;
    }
    
    @Override
    public boolean renderSkin(ISkinPointer skinPointer) {
        ISkinType skinType= skinPointer.getSkinType();
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            //TODO Offset each part when rendering.
            ISkinPartType skinPartType = skinType.getSkinParts().get(i);
            renderSkinPart(skinPointer, skinPartType);
        }
        return false;
    }
    
    @Override
    public boolean renderSkinPart(ISkinPointer skinPointer, ISkinPartType skinPartType) {
        if (skinPointer == null | skinPartType == null) {
            return false;
        }
        Skin skin = ClientModelCache.INSTANCE.getEquipmentItemData(skinPointer.getSkinId());
        if (skin == null) {
            return false;
        }
        skin.onUsed();
        for (int i = 0; i < skin.getParts().size(); i++) {
            SkinPart skinPart = skin.getParts().get(i);
            if (skinPart.getPartType() == skinPartType) {
                EquipmentPartRenderer.INSTANCE.renderPart(skinPart, 0.0625F);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSkinInModelCache(ItemStack stack) {
        if (!EquipmentNBTHelper.stackHasSkinData(stack)) {
            return false;
        }
        SkinPointer skinPointer = EquipmentNBTHelper.getSkinPointerFromStack(stack);
        return isSkinInModelCache(skinPointer);
    }
    
    @Override
    public boolean isSkinInModelCache(ISkinPointer skinPointer) {
        if (skinPointer == null) {
            return false;
        }
        return ClientModelCache.INSTANCE.isEquipmentInCache(skinPointer.getSkinId());
    }
    
    @Override
    public void requestSkinModelFromSever(ItemStack stack) {
        if (!EquipmentNBTHelper.stackHasSkinData(stack)) {
            return;
        }
        SkinPointer skinPointer = EquipmentNBTHelper.getSkinPointerFromStack(stack);
        requestSkinModelFromSever(skinPointer);
    }

    @Override
    public void requestSkinModelFromSever(ISkinPointer skinPointer) {
        if (skinPointer == null) {
            return;
        }
        ClientModelCache.INSTANCE.requestEquipmentDataFromServer(skinPointer.getSkinId());
    }

    @Override
    public ModelBase getArmourerHandModel() {
        return ModelHand.MODEL;
    }

    @Override
    public ISkin getSkinFromModelCache(ISkinPointer skinPointer) {
        if (skinPointer == null) {
            return null;
        }
        return ClientModelCache.INSTANCE.getEquipmentItemData(skinPointer.getSkinId());
    }
    
    @Override
    public boolean isArmourRenderOverridden(EntityPlayer player, int slotId) {
        if (slotId < 4 & slotId >= 0) {
            return false;
        }
        if (player == null) {
            return false;
        }
        EquipmentWardrobeHandler ewh = ClientProxy.equipmentWardrobeHandler;
        EquipmentWardrobeData ewd = ewh.getEquipmentWardrobeData(new PlayerPointer(player));
        if (ewd != null) {
            return ewd.armourOverride.get(slotId);
        }
        return false;
    }
}
