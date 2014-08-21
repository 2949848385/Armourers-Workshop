package riskyken.armourersWorkshop.client.model;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.common.customarmor.AbstractCustomArmour;
import riskyken.armourersWorkshop.common.customarmor.ArmourBlockData;
import riskyken.armourersWorkshop.common.customarmor.ArmourPart;
import riskyken.armourersWorkshop.common.customarmor.ArmourerType;
import riskyken.armourersWorkshop.proxies.ClientProxy;

public class ModelCustomArmourLegs extends ModelBiped {
    
    private ModelRenderer main;
    
    public ModelCustomArmourLegs() {
        main = new ModelRenderer(this, 28, 20);
        main.addBox(0F, 0F, 0F, 1, 1, 1);
        main.setRotationPoint(0, 0, 0);
    }
    
    @Override
    public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entity);
        AbstractCustomArmour skirtData = ClientProxy.getPlayerCustomArmour(entity, ArmourerType.LEGS, ArmourPart.SKIRT);
        AbstractCustomArmour leftLegData = ClientProxy.getPlayerCustomArmour(entity, ArmourerType.LEGS, ArmourPart.LEFT_LEG);
        AbstractCustomArmour rightLegData = ClientProxy.getPlayerCustomArmour(entity, ArmourerType.LEGS, ArmourPart.RIGHT_LEG);
        EntityClientPlayerMP player = (EntityClientPlayerMP) entity;
        
        this.isSneak = player.isSneaking();
        this.isRiding = player.isRiding();
        this.heldItemRight = 0;
        if (player.getHeldItem() != null) {
            this.heldItemRight = 1;
        }
        
        bindPlayerTexture();
        
        if (skirtData != null){
            GL11.glPushMatrix();
            GL11.glColor3f(0F, 0F, 0F);
            
            GL11.glRotatef((float) RadiansToDegrees(this.bipedBody.rotateAngleX), 1, 0, 0);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedBody.rotateAngleY), 0, 1, 0);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedBody.rotateAngleZ), 0, 0, 1);
            GL11.glTranslated(0, 11 * scale, 0);
            renderPart(skirtData.getArmourData(), scale);
            GL11.glPopMatrix();
        }
        
        
        if (leftLegData != null){
            GL11.glPushMatrix();
            
            GL11.glTranslated(2 * scale, 11 * scale, 0);
            //GL11.glTranslated(0 * scale, 4 * scale, 0 * scale);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedLeftLeg.rotateAngleX), 1, 0, 0);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedLeftLeg.rotateAngleY), 0, 1, 0);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedLeftLeg.rotateAngleZ), 0, 0, 1);
            //GL11.glTranslated(0 * scale, -4 * scale, 0 * scale);
            GL11.glColor3f(0F, 0F, 0F);
            
            renderPart(leftLegData.getArmourData(), scale);
            GL11.glPopMatrix();
        }
        
        if (rightLegData != null){
            GL11.glPushMatrix();
            GL11.glColor3f(0F, 0F, 0F);
            GL11.glTranslated(-2 * scale, 11 * scale, 0);
            //GL11.glTranslated(0 * scale, 4 * scale, 0 * scale);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedRightLeg.rotateAngleX), 1, 0, 0);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedRightLeg.rotateAngleY), 0, 1, 0);
            GL11.glRotatef((float) RadiansToDegrees(this.bipedRightLeg.rotateAngleZ), 0, 0, 1);
            //GL11.glTranslated(0 * scale, -4 * scale, 0 * scale);
            
            renderPart(rightLegData.getArmourData(), scale);
            GL11.glPopMatrix();
        }
        
    }
    
    private static double RadiansToDegrees(double angle)
    {
       return angle * (180.0 / Math.PI);
    }
    
    private void renderPart(ArrayList<ArmourBlockData> armourBlockData, float scale) {
        for (int i = 0; i < armourBlockData.size(); i++) {
            ArmourBlockData blockData = armourBlockData.get(i);
            if (!blockData.glowing) {
                renderArmourBlock(blockData.x, blockData.y, blockData.z, blockData.colour, scale);
            }
        }
        
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        
        for (int i = 0; i < armourBlockData.size(); i++) {
            ArmourBlockData blockData = armourBlockData.get(i);
            if (blockData.glowing) {
                renderArmourBlock(blockData.x, blockData.y, blockData.z, blockData.colour, scale);
            }
        }
        
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderArmourBlock(int x, int y, int z, int colour, float scale) {
        float colourRed = (colour >> 16 & 0xff) / 255F;
        float colourGreen = (colour >> 8 & 0xff) / 255F;
        float colourBlue = (colour & 0xff) / 255F;

        GL11.glPushMatrix();

        GL11.glColor3f(colourRed, colourGreen, colourBlue);

        // ModLogger.log(x + " " + y + " " + z);
        GL11.glTranslated(x * scale, y * scale, z * scale);
        main.render(scale);
        GL11.glPopMatrix();
    }

    private void bindPlayerTexture() {
        ResourceLocation playerSkin = Minecraft.getMinecraft().thePlayer.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(playerSkin);
    }
}
