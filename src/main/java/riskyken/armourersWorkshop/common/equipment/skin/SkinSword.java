package riskyken.armourersWorkshop.common.equipment.skin;

import java.util.ArrayList;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SkinSword implements ISkinType {

    private ArrayList<ISkinPart> skinParts;
    
    public SkinSword() {
        this.skinParts = new ArrayList<ISkinPart>();
        skinParts.add(new SkinSwordPartBase());
    }
    
    @Override
    public ArrayList<ISkinPart> getSkinParts() {
        return this.skinParts;
    }
    
    @Override
    public String getRegistryName() {
        return "armourers:sword";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderBuildingGuide(float scale, boolean showSkinOverlay, boolean showHelper) {
        for (int i = 0; i < this.skinParts.size(); i++) {
            ISkinPart skinPart = this.skinParts.get(i);
            skinPart.renderBuildingGuide(scale, showSkinOverlay, showHelper);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void renderBuildingGrid(float scale) {
        for (int i = 0; i < this.skinParts.size(); i++) {
            ISkinPart skinPart = this.skinParts.get(i);
            skinPart.renderBuildingGrid(scale);
        }
    }

    @Override
    public void createBoundingBoxes(World world, int x, int y, int z) {
        // TODO Auto-generated method stub
    }

    @Override
    public void removeBoundingBoxed(World world, int x, int y, int z) {
        // TODO Auto-generated method stub
    }

    @Override
    public int clearArmourCubes() {
        return 0;
    }

    @Override
    public boolean showSkinOverlayCheckbox() {
        // TODO Auto-generated method stub
        return false;
    }
}
