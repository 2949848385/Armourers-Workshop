package riskyken.armourers_workshop.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import riskyken.armourers_workshop.api.client.render.entity.ISkinnableEntityRenderer;
import riskyken.armourers_workshop.api.common.skin.IEntityEquipment;
import riskyken.armourers_workshop.client.handler.ModClientFMLEventHandler;

@SideOnly(Side.CLIENT)
public class SkinnableEntityCreeperRenderer implements ISkinnableEntityRenderer<EntityCreeper> {

    //@Override
    public void render(EntityCreeper entity, RenderEntity renderer,
            double x, double y, double z, IEntityEquipment entityEquipment) {
        GL11.glPushMatrix();
        float scale = 0.0625F;
        
        GL11.glTranslated(x, y, z);
        GL11.glScalef(1, -1, -1);
        
        double rot = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * ModClientFMLEventHandler.renderTickTime;
        GL11.glRotated(rot, 0, 1, 0);
        
        if (entity.deathTime > 0) {
            float angle = ((float)entity.deathTime + ModClientFMLEventHandler.renderTickTime - 1.0F) / 20.0F * 1.6F;
            angle = MathHelper.sqrt(angle);
            if (angle > 1.0F) {
                angle = 1.0F;
            }
            GL11.glRotatef(angle * 90F, 0.0F, 0.0F, 1.0F);
        }
        
        GL11.glTranslated(0, -20.2F * scale, 0);
        //GL11.glTranslated(0, 0, -4.0F * scale);
        
        double headRot = entity.prevRotationYawHead + (entity.rotationYawHead - entity.prevRotationYawHead) * ModClientFMLEventHandler.renderTickTime;
        GL11.glRotated(headRot - rot, 0, 1, 0);
        GL11.glRotatef(entity.rotationPitch, 1, 0, 0);
        
        float headScale = 1.001F;
        GL11.glScalef(headScale, headScale, headScale);
        //renderEquipmentType(entity, renderer, SkinTypeRegistry.skinHead, entityEquipment);
        GL11.glPopMatrix();
    }
    /*
    private void renderEquipmentType(EntityLivingBase entity, RendererLivingEntity renderer, ISkinType skinType, IEntityEquipment equipmentData) {
        if (equipmentData.haveEquipment(skinType, 0)) {
            ISkinPointer skinPointer = equipmentData.getSkinPointer(skinType, 0);
            Skin skin = ClientSkinCache.INSTANCE.getSkin(skinPointer);
            if (skin == null) {
                return;
            }
            GL11.glEnable(GL11.GL_NORMALIZE);
            float scale = 0.0625F;
            for (int i = 0; i < skin.getParts().size(); i++) {
                SkinPartRenderer.INSTANCE.renderPart(skin.getParts().get(i), scale, skinPointer.getSkinDye(), null, false);
            }
            GL11.glDisable(GL11.GL_NORMALIZE);
        }
    }*/
}
