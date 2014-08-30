package riskyken.armourersWorkshop.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.client.model.ModelChest;
import riskyken.armourersWorkshop.client.model.ModelHead;
import riskyken.armourersWorkshop.client.model.ModelLegs;
import riskyken.armourersWorkshop.common.customarmor.ArmourPart;
import riskyken.armourersWorkshop.common.customarmor.ArmourerType;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.tileentities.TileEntityArmourerBrain;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockArmourer extends TileEntitySpecialRenderer {
    
    private static final ResourceLocation guideImage = new ResourceLocation(LibModInfo.ID.toLowerCase(), "textures/blocks/guide.png");
    private static final ModelHead modelHead = new ModelHead();
    private static final ModelChest modelChest = new ModelChest();
    private static final ModelLegs modelLegs = new ModelLegs();
    // private static ModelFeet modelFeet = new ModelFeet();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tickTime) {

        TileEntityArmourerBrain te = (TileEntityArmourerBrain) tileEntity;
        ArmourerType type = te.getType();
        
        if (!te.isFormed()) { return; }

        this.bindTexture(Minecraft.getMinecraft().thePlayer.getLocationSkin());

        GL11.glPushMatrix();
        GL11.glColor3f(1F, 1F, 1F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glTranslated(x + te.getXOffset() + 11, y, z + te.getZOffset() + 11);
        
        GL11.glScalef(-1, -1, 1);
        GL11.glScalef(16, 16, 16);
        
        switch (type) {
        case HEAD:
            modelHead.render();
            break;
        case CHEST:
            modelChest.render();
            break;
        case LEGS:
            modelLegs.render(te.isSkirtMode());
            break;
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glColor3f(1F, 1F, 1F);
        
        if (te.isShowGuides()) {
            renderGuide(te, type, x + te.getXOffset(), y, z + te.getZOffset());
        }
    }
    
    private void renderGuide(TileEntityArmourerBrain te, ArmourerType type, double x, double y, double z) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(guideImage);
        switch (type) {
        case HEAD:
            renderGuidePart(ArmourPart.HEAD, x, y, z);
            break;
        case CHEST:
            renderGuidePart(ArmourPart.CHEST, x, y, z);
            renderGuidePart(ArmourPart.LEFT_ARM, x, y, z);
            renderGuidePart(ArmourPart.RIGHT_ARM, x, y, z);
            break;
        case LEGS:
            if (te.isSkirtMode()) {
                renderGuidePart(ArmourPart.SKIRT, x, y, z);
            } else {
                renderGuidePart(ArmourPart.LEFT_LEG, x, y, z);
                renderGuidePart(ArmourPart.RIGHT_LEG, x, y, z);
            }
            break;
        }
    }
    
    private void renderGuidePart(ArmourPart part, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        renderGuideFace(ForgeDirection.SOUTH, x + part.getXOffset(), y + 1, z  + part.getZOffset(), part.getXSize(), part.getYSize());
        renderGuideFace(ForgeDirection.EAST, x + part.getXOffset(), y + 1, z  + part.getZOffset() + part.getZSize(), part.getZSize(), part.getYSize());
        renderGuideFace(ForgeDirection.WEST, x + part.getXOffset() + part.getXSize(), y + 1, z  + part.getZOffset(), part.getZSize(), part.getYSize());
        renderGuideFace(ForgeDirection.NORTH, x + part.getXOffset() + part.getXSize(), y + 1, z  + part.getZOffset() + part.getZSize(), part.getXSize(), part.getYSize());
        renderGuideFace(ForgeDirection.UP, x + part.getXOffset(), y + 1 + part.getYSize(), z  + part.getZOffset(), part.getXSize(), part.getZSize());
        renderGuideFace(ForgeDirection.DOWN, x + part.getXOffset(), y + 1, z  + part.getZOffset() + part.getZSize(), part.getXSize(), part.getZSize());
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    
    private void renderGuideFace(ForgeDirection dir, double x, double y, double z, int sizeX, int sizeY) {
        RenderManager renderManager = RenderManager.instance;
        Tessellator tessellator = Tessellator.instance;
        
        GL11.glPushMatrix();

        
        GL11.glTranslated(x, y, z);
        
        switch (dir) {
        case EAST:
            GL11.glRotated(90, 0, 1, 0);
            break;
        case WEST:
            GL11.glRotated(-90, 0, 1, 0);
            break;
        case NORTH:
            GL11.glRotated(180, 0, 1, 0);
            break; 
        case UP:
            GL11.glRotated(90, 1, 0, 0);
            break;
        case DOWN:
            GL11.glRotated(-90, 1, 0, 0);
            break;
        default:
            break;
        }
        
        tessellator.setBrightness(15728880);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(0, sizeY, 0, sizeY, 0);
        tessellator.addVertexWithUV(sizeX, sizeY, 0, sizeY, sizeX);
        tessellator.addVertexWithUV(sizeX, 0, 0, 0, sizeX);
        tessellator.draw();
        
        GL11.glRotatef(180, 0, 1, 0);
        GL11.glTranslated(-sizeX, 0, 0);
        
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(0, sizeY, 0, sizeY, 0);
        tessellator.addVertexWithUV(sizeX, sizeY, 0, sizeY, sizeX);
        tessellator.addVertexWithUV(sizeX, 0, 0, 0, sizeX);
        tessellator.draw();

        GL11.glPopMatrix();
    }
}
