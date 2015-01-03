package riskyken.armourersWorkshop.client.model.equipmet;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import riskyken.armourersWorkshop.client.render.EquipmentPartRenderer;
import riskyken.armourersWorkshop.common.equipment.data.CustomEquipmentPartData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractModelCustomEquipment extends ModelBiped implements IEquipmentModel {

    protected static float SCALE = 0.0625F;
    
    protected void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    protected void setRotation(ModelRenderer targetModel, ModelRenderer sourceModel) {
        targetModel.rotateAngleX = sourceModel.rotateAngleX;
        targetModel.rotateAngleY = sourceModel.rotateAngleY;
        targetModel.rotateAngleZ = sourceModel.rotateAngleZ;
    }
    
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.isRiding = false;
        this.isSneak = false;
        this.aimedBow = false;
        this.isChild = false;
        this.heldItemRight = 0;
        bipedLeftLeg.rotateAngleZ = 0F;
        bipedRightLeg.rotateAngleZ = 0F;
        bipedHead.rotateAngleZ = 0F;
        bipedHeadwear.rotateAngleZ = 0F;
    }
    
    protected void setRotationFromModelBiped(ModelBiped modelBiped) {
        this.isRiding = false;
        this.isSneak = false;
        this.aimedBow = false;
        this.heldItemRight = 0;
        if (modelBiped == null) {
            setRotation(bipedHead, 0F, 0F, 0F);
            setRotation(bipedBody, 0F, 0F, 0F);
            setRotation(bipedLeftArm, 0F, 0F, 0F);
            setRotation(bipedRightArm, 0F, 0F, 0F);
            setRotation(bipedLeftLeg, 0F, 0F, 0F);
            setRotation(bipedRightLeg, 0F, 0F, 0F);
            isChild = false;
        } else {
            setRotation(bipedHead, modelBiped.bipedHead);
            setRotation(bipedBody, modelBiped.bipedBody);
            setRotation(bipedLeftArm, modelBiped.bipedLeftArm);
            setRotation(bipedRightArm, modelBiped.bipedRightArm);
            setRotation(bipedLeftLeg, modelBiped.bipedLeftLeg);
            setRotation(bipedRightLeg, modelBiped.bipedRightLeg);
            isChild = modelBiped.isChild;
        }
    }
    
    protected void renderPart(CustomEquipmentPartData armourPart, float scale) {
        EquipmentPartRenderer.INSTANCE.renderPart(armourPart, scale);
    }
}
