package riskyken.armourersWorkshop.client.model;

import java.util.ArrayList;
import java.util.BitSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.client.model.custom.equipment.CustomModelRenderer;
import riskyken.armourersWorkshop.common.equipment.data.CustomEquipmentBlockData;
import riskyken.armourersWorkshop.common.equipment.data.CustomEquipmentPartData;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.utils.ModLogger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCustomArmour extends ModelBiped{
    
    private static final ResourceLocation texture = new ResourceLocation(LibModInfo.ID.toLowerCase(), "textures/armour/cube.png");
    protected static float scale = 0.0625F;
    private final CustomModelRenderer main;
    
    public ModelCustomArmour() {
        textureWidth = 4;
        textureHeight = 4;
        
        main = new CustomModelRenderer(this, 0, 0);
        main.addBox(0F, 0F, 0F, 1, 1, 1);
        main.setRotationPoint(0, 0, 0);
    }
    
    public static double RadiansToDegrees(double angle) {
        return angle * (180.0 / Math.PI);
     }
    
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
        this.heldItemRight = 0;
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
     
    public void renderPart(CustomEquipmentPartData armourPart, float scale) {
        // GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_LINE );
        GL11.glColor3f(1F, 1F, 1F);
        if (!armourPart.displayNormalCompiled) {
            if (hasNormalBlocks(armourPart.getArmourData())) {
                armourPart.hasNormalBlocks = true;
                armourPart.displayListNormal = GLAllocation.generateDisplayLists(1);
                GL11.glNewList(armourPart.displayListNormal, GL11.GL_COMPILE);
                GL11.glPushMatrix();
                this.renderNomralPartBlocks(armourPart.getArmourData(), scale);
                GL11.glPopMatrix();
                GL11.glEndList();
            }
            armourPart.displayNormalCompiled = true;
        }
        
        if (!armourPart.displayGlowingCompiled) {
            if (hasGlowingBlocks(armourPart.getArmourData())) {
                armourPart.hasGlowingBlocks = true;
                armourPart.displayListGlowing = GLAllocation.generateDisplayLists(1);
                GL11.glNewList(armourPart.displayListGlowing, GL11.GL_COMPILE);
                GL11.glPushMatrix();
                this.renderGlowingPartBlocks(armourPart.getArmourData(), scale);
                GL11.glPopMatrix();
                GL11.glEndList();
            }
            armourPart.displayGlowingCompiled = true;
        }
        
        if (armourPart.hasNormalBlocks) {
            GL11.glCallList(armourPart.displayListNormal);
        }
        
        if (armourPart.hasGlowingBlocks) {
            float lastBrightnessX = OpenGlHelper.lastBrightnessX;
            float lastBrightnessY = OpenGlHelper.lastBrightnessY;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            GL11.glCallList(armourPart.displayListGlowing);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        }
        GL11.glColor3f(1F, 1F, 1F);
        // GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_FILL );
    }
    
    private boolean hasNormalBlocks(ArrayList<CustomEquipmentBlockData> armourBlockData) {
        for (int i = 0; i < armourBlockData.size(); i++) {
            CustomEquipmentBlockData blockData = armourBlockData.get(i);
            if (!blockData.isGlowing()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasGlowingBlocks(ArrayList<CustomEquipmentBlockData> armourBlockData) {
        for (int i = 0; i < armourBlockData.size(); i++) {
            CustomEquipmentBlockData blockData = armourBlockData.get(i);
            if (blockData.isGlowing()) {
                return true;
            }
        }
        return false;
    }
    
    private void renderNomralPartBlocks(ArrayList<CustomEquipmentBlockData> armourBlockData, float scale) {
        GL11.glPushMatrix();
        for (int i = 0; i < armourBlockData.size(); i++) {
            CustomEquipmentBlockData blockData = armourBlockData.get(i);
            if (!blockData.isGlowing()) {
                renderArmourBlock(blockData.x, blockData.y, blockData.z, blockData.colour, scale, blockData.faceFlags);
            }
        }
        GL11.glPopMatrix();
    }
    
    private void renderGlowingPartBlocks(ArrayList<CustomEquipmentBlockData> armourBlockData, float scale) {
        GL11.glPushMatrix();
        for (int i = 0; i < armourBlockData.size(); i++) {
            CustomEquipmentBlockData blockData = armourBlockData.get(i);
            if (blockData.isGlowing()) {
                renderArmourBlock(blockData.x, blockData.y, blockData.z, blockData.colour, scale, blockData.faceFlags);
            }
        }
        GL11.glPopMatrix();
    }

    public void renderArmourBlock(int x, int y, int z, int colour, float scale, BitSet faceFlags) {
        float colourRed = (colour >> 16 & 0xff) / 255F;
        float colourGreen = (colour >> 8 & 0xff) / 255F;
        float colourBlue = (colour & 0xff) / 255F;

        GL11.glPushMatrix();
        GL11.glColor3f(colourRed, colourGreen, colourBlue);
        GL11.glTranslated(x * scale, y * scale, z * scale);
        if (faceFlags != null) {
            main.render(scale, faceFlags);
        } else {
            ModLogger.log(Level.WARN, "No face flags found on armour part.");
        }

        GL11.glPopMatrix();
    }

    public double DegreesToRadians(double degrees) {
        return 2 * Math.PI * degrees / 360.0;
    }

    public void bindArmourTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }
}
