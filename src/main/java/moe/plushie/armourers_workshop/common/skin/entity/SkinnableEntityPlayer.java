package moe.plushie.armourers_workshop.common.skin.entity;

import java.util.ArrayList;
import java.util.List;

import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.client.render.entity.ModelResetLayer;
import moe.plushie.armourers_workshop.client.render.entity.SkinLayerRendererPlayer;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import moe.plushie.armourers_workshop.utils.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkinnableEntityPlayer extends SkinnableEntity {

    @Override
    public Class<? extends EntityLivingBase> getEntityClass() {
        return EntityPlayer.class;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addRenderLayer(RenderManager renderManager) {
        for (RenderPlayer playerRender : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            try {
                Object object = ReflectionHelper.getPrivateValue(RenderLivingBase.class, playerRender, "field_177097_h", "layerRenderers");
                if (object != null) {
                    List<LayerRenderer<?>> layerRenderers = (List<LayerRenderer<?>>) object;
                    layerRenderers.add(0, new ModelResetLayer(playerRender));
                    ModLogger.log("adding reset layer");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            playerRender.addLayer(new SkinLayerRendererPlayer(playerRender));
        }
    }

    @Override
    public void getValidSkinTypes(ArrayList<ISkinType> skinTypes) {
        skinTypes.add(SkinTypeRegistry.skinHead);
        skinTypes.add(SkinTypeRegistry.skinChest);
        skinTypes.add(SkinTypeRegistry.skinLegs);
        skinTypes.add(SkinTypeRegistry.skinFeet);
        skinTypes.add(SkinTypeRegistry.skinWings);
        
        skinTypes.add(SkinTypeRegistry.skinSword);
        skinTypes.add(SkinTypeRegistry.skinShield);
        skinTypes.add(SkinTypeRegistry.skinBow);
        
        skinTypes.add(SkinTypeRegistry.skinPickaxe);
        skinTypes.add(SkinTypeRegistry.skinAxe);
        skinTypes.add(SkinTypeRegistry.skinShovel);
        skinTypes.add(SkinTypeRegistry.skinHoe);
    }
    
    @Override
    public int getSlotsForSkinType(ISkinType skinType) {
        if (skinType.getVanillaArmourSlotId() != -1) {
            return 8;
        }
        if (skinType == SkinTypeRegistry.skinWings) {
            return 8;
        }
        return 1;
    }
}
