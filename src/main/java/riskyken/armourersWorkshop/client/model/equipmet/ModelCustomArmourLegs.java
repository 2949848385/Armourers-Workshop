package riskyken.armourersWorkshop.client.model.equipmet;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinDye;
import riskyken.armourersWorkshop.common.ApiRegistrar;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;

@SideOnly(Side.CLIENT)
public class ModelCustomArmourLegs extends AbstractModelCustomEquipment {
    
    @Override
    public void render(Entity entity, Skin armourData, float limb1, float limb2, float limb3, float headY, float headX) {
        setRotationAngles(limb1, limb2, limb3, headY, headX, SCALE, entity);
        render(entity, armourData, false, null);
    }
    
    @Override
    public void render(Entity entity, ModelBiped modelBiped, Skin armourData, boolean showSkinPaint, ISkinDye skinDye) {
        setRotationFromModelBiped(modelBiped);
        render(entity, armourData, showSkinPaint, skinDye);
    }
    
    @Override
    public void render(Entity entity, Skin armourData, boolean showSkinPaint, ISkinDye skinDye) {
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

        
        ApiRegistrar.INSTANCE.onRenderEquipment(entity, SkinTypeRegistry.skinLegs);
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
            
            if (part.getPartType().getPartName().equals("leftLeg")) {
                renderLeftLeg(part, SCALE, skinDye);
            } else if (part.getPartType().getPartName().equals("rightLeg")) {
                renderRightLeg(part, SCALE, skinDye);
            } else if (part.getPartType().getPartName().equals("skirt")) {
                renderSkirt(part, SCALE, skinDye);
            }
            
            GL11.glPopMatrix();
        }
        
        GL11.glColor3f(1F, 1F, 1F);
    }
    
    private void renderLeftLeg(SkinPart part, float scale, ISkinDye skinDye) {
        GL11.glPushMatrix();
        if (isSneak) {
            GL11.glTranslated(0, -3 * scale, 4 * scale);
        }
        GL11.glColor3f(1F, 1F, 1F);
        GL11.glTranslated(2 * scale, 12 * scale, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedLeftLeg.rotateAngleZ), 0, 0, 1);
        GL11.glRotatef((float) Math.toDegrees(this.bipedLeftLeg.rotateAngleY), 0, 1, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedLeftLeg.rotateAngleX), 1, 0, 0);
        renderPart(part, scale, skinDye);
        GL11.glPopMatrix();
    }
    
    private void renderRightLeg(SkinPart part, float scale, ISkinDye skinDye) {
        GL11.glPushMatrix();
        if (isSneak) {
            GL11.glTranslated(0, -3 * scale, 4 * scale);
        }
        GL11.glColor3f(1F, 1F, 1F);
        GL11.glTranslated(-2 * scale, 12 * scale, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedRightLeg.rotateAngleZ), 0, 0, 1);
        GL11.glRotatef((float) Math.toDegrees(this.bipedRightLeg.rotateAngleY), 0, 1, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedRightLeg.rotateAngleX), 1, 0, 0);
        renderPart(part, scale, skinDye);
        GL11.glPopMatrix();
    }
    
    private void renderSkirt(SkinPart part, float scale, ISkinDye skinDye) {
        GL11.glPushMatrix();
        GL11.glColor3f(1F, 1F, 1F);
      
        GL11.glTranslated(0, 12 * scale, 0);
        GL11.glRotatef((float) Math.toDegrees(this.bipedBody.rotateAngleY), 0, 1, 0);
        if (isSneak) {
            GL11.glTranslated(0, -3 * scale, 4 * scale);
        }
        
        renderPart(part, scale, skinDye);
        GL11.glPopMatrix();
    }
}
