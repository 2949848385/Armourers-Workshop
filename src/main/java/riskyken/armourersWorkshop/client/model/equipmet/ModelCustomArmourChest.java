package riskyken.armourersWorkshop.client.model.equipmet;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.common.ApiRegistrar;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCustomArmourChest extends AbstractModelCustomEquipment {
    
    @Override
    public void render(Entity entity, Skin armourData, float limb1, float limb2, float limb3, float headY, float headX) {
        setRotationAngles(limb1, limb2, limb3, headY, headX, SCALE, entity);
        render(entity, armourData);
    }
    
    @Override
    public void render(Entity entity, ModelBiped modelBiped, Skin armourData) {
        setRotationFromModelBiped(modelBiped);
        render(entity, armourData);
    }
    
    @Override
    public void render(Entity entity, Skin armourData) {
        if (armourData == null) { return; }
        ArrayList<SkinPart> parts = armourData.getParts();
        
        if (entity != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            this.isSneak = player.isSneaking();
            this.isRiding = player.isRiding();
            this.heldItemRight = 0;
            if (player.getHeldItem() != null) {
                this.heldItemRight = 1;
            }
        }
        
        ApiRegistrar.INSTANCE.onRenderEquipment(entity, SkinTypeRegistry.skinChest);
        armourData.onUsed();
        
        for (int i = 0; i < parts.size(); i++) {
            SkinPart part = parts.get(i);
            
            GL11.glPushMatrix();
            if (isChild) {
                float f6 = 2.0F;
                GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
                GL11.glTranslatef(0.0F, 24.0F * SCALE, 0.0F);
            }

            ApiRegistrar.INSTANCE.onRenderEquipmentPart(entity, part.getPartType());
            
            if (part.getPartType().getPartName().equals("base")) {
                renderChest(part, SCALE);
            } else if (part.getPartType().getPartName().equals("leftArm")) {
                renderLeftArm(part, SCALE);
            } else if (part.getPartType().getPartName().equals("rightArm")) {
                renderRightArm(part, SCALE);
            }
            
            GL11.glPopMatrix();
            
        }
        
        GL11.glColor3f(1F, 1F, 1F);
    }
    
    private void renderChest(SkinPart part, float scale) {
        GL11.glPushMatrix();
        GL11.glColor3f(1F, 1F, 1F);
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleX), 1, 0, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleY), 0, 1, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleZ), 0, 0, 1);
        renderPart(part, scale);
        GL11.glPopMatrix();
    }
    
    private void renderLeftArm(SkinPart part, float scale) {
        GL11.glPushMatrix();
        
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleZ), 0, 0, 1);
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleY), 0, 1, 0);
        //GL11.glRotatef((float) RadiansToDegrees(this.bipedBody.rotateAngleX), 1, 0, 0);
        
        GL11.glTranslatef(5.0F * scale, 0F, 0F);
        GL11.glTranslatef(0F, 2.0F * scale, 0F);
        
        
        GL11.glRotatef((float) Math.toDegrees(this.bipedLeftArm.rotateAngleZ), 0, 0, 1);
        GL11.glRotatef((float) Math.toDegrees(this.bipedLeftArm.rotateAngleY), 0, 1, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedLeftArm.rotateAngleX), 1, 0, 0);
        
        renderPart(part, scale);
        
        GL11.glPopMatrix();
    }
    
    private void renderRightArm(SkinPart part, float scale) {
        GL11.glPushMatrix();
        
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleZ), 0, 0, 1);
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleY), 0, 1, 0);
        //GL11.glRotatef((float) RadiansToDegrees(this.bipedBody.rotateAngleX), 1, 0, 0);
        
        GL11.glTranslatef(-5.0F * scale, 0F, 0F);
        GL11.glTranslatef(0F, 2.0F * scale, 0F);
        
        GL11.glRotatef((float) Math.toDegrees(this.bipedRightArm.rotateAngleZ), 0, 0, 1);
        GL11.glRotatef((float) Math.toDegrees(this.bipedRightArm.rotateAngleY), 0, 1, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedRightArm.rotateAngleX), 1, 0, 0);
        
        renderPart(part, scale);
        GL11.glPopMatrix();
    }
}
