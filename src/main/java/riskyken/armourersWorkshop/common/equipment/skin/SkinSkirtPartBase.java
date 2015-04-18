package riskyken.armourersWorkshop.common.equipment.skin;

import javax.vecmath.Point3i;

import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.client.model.armourer.ModelLegs;
import riskyken.armourersWorkshop.client.render.block.RenderBlockMiniArmourer;
import riskyken.armourersWorkshop.common.Rectangle3D;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SkinSkirtPartBase implements ISkinPart {

    @SideOnly(Side.CLIENT)
    private static final ModelLegs partModel = new ModelLegs();
    
    private Rectangle3D buildingSpace;
    private Rectangle3D guideSpace;
    private Point3i offset;
    private Point3i origin;
    
    public SkinSkirtPartBase() {
        this.buildingSpace = new Rectangle3D(-10, -12, -10, 20, 15, 20);
        this.guideSpace = new Rectangle3D(-4, -12, -2, 8, 12, 4);
        this.offset = new Point3i(0, 0, 0);
        this.origin = new Point3i(0, 12, 0);
    }
    
    @Override
    public Rectangle3D getBuildingSpace() {
        return this.buildingSpace;
    }

    @Override
    public Rectangle3D getGuideSpace() {
        return this.guideSpace;
    }

    @Override
    public Point3i getOffset() {
        return this.offset;
    }

    @Override
    public Point3i getOrigin() {
        return this.origin;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderBuildingGuide(float scale,boolean showSkinOverlay, boolean showHelper) {
        GL11.glTranslated(offset.x * scale, offset.y * scale, offset.z * scale);
        GL11.glTranslated(0, this.buildingSpace.y * scale, 0);
        GL11.glTranslated(origin.x * scale, origin.y * scale, origin.z * scale);
        GL11.glTranslated(2 * scale, 0, 0);
        partModel.renderLeftLeft(scale);
        GL11.glTranslated(-4 * scale, 0, 0);
        partModel.renderRightLeg(scale);
        GL11.glTranslated(2 * scale, 0, 0);
        GL11.glTranslated(-origin.x * scale, -origin.y * scale, -origin.z * scale);
        GL11.glTranslated(0, -this.buildingSpace.y * scale, 0);
        GL11.glTranslated(-offset.x * scale, -offset.y * scale, -offset.z * scale);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void renderBuildingGrid(float scale) {
        GL11.glTranslated(offset.x * scale, offset.y * scale, offset.z * scale);
        GL11.glTranslated(0, this.buildingSpace.y * scale, 0);
        GL11.glScalef(-1, -1, 1);
        RenderBlockMiniArmourer.renderGuidePart(this, scale);
        GL11.glScalef(-1, -1, 1);
        GL11.glTranslated(0, -this.buildingSpace.y * scale, 0);
        GL11.glTranslated(-offset.x * scale, -offset.y * scale, -offset.z * scale);
    }
    
    @Override
    public void removeBoundingBoxesForPart(World world, int x, int y, int z) {
        // TODO Auto-generated method stub
    }

    @Override
    public void createBoundingBoxesForPart(World world, int x, int y, int z) {
        // TODO Auto-generated method stub
    }
}
