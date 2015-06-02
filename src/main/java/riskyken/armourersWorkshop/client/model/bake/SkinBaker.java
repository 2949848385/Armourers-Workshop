package riskyken.armourersWorkshop.client.model.bake;

import java.util.ArrayList;
import java.util.BitSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraftforge.common.util.ForgeDirection;
import riskyken.armourersWorkshop.client.render.EquipmentPartRenderer;
import riskyken.armourersWorkshop.common.config.ConfigHandler;
import riskyken.armourersWorkshop.common.skin.cubes.CubeFactory;
import riskyken.armourersWorkshop.common.skin.cubes.ICube;
import riskyken.armourersWorkshop.common.skin.cubes.ICubeColour;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;

public final class SkinBaker {
    
    public static boolean withinMaxRenderDistance(double x, double y, double z) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player.getDistance(x, y, z) > ConfigHandler.maxRenderDistance) {
            return false;
        }
        return true;
    }
    
    public static void cullFacesOnEquipmentPart(SkinPart partData) {
        ArrayList<ICube> blocks = partData.getArmourData();
        partData.totalCubesInPart = new int[CubeFactory.INSTANCE.getTotalCubes()];
        for (int i = 0; i < blocks.size(); i++) {
            ICube blockData = blocks.get(i);
            int cubeId = CubeFactory.INSTANCE.getIdForCubeClass(blockData.getClass());
            partData.totalCubesInPart[cubeId] += 1;
            setBlockFaceFlags(blocks, blockData);
        }
    }
    
    private static void setBlockFaceFlags(ArrayList<ICube> partBlocks, ICube block) {
        block.setFaceFlags(new BitSet(6));
        for (int j = 0; j < partBlocks.size(); j++) {
            ICube checkBlock = partBlocks.get(j);
            checkFaces(block, checkBlock);
        }
    }
    
    private static void checkFaces(ICube block, ICube checkBlock) {
        ForgeDirection[] dirs = {ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST };
        //dirs = ForgeDirection.VALID_DIRECTIONS;
        for (int i = 0; i < dirs.length; i++) {
            ForgeDirection dir = dirs[i];
            if (block.getX() + dir.offsetX == checkBlock.getX()) {
                if (block.getY() + dir.offsetY == checkBlock.getY()) {
                    if (block.getZ() + dir.offsetZ == checkBlock.getZ()) {
                        block.getFaceFlags().set(i, true); 
                    }
                }
            }
        }

    }
    
    public static void buildPartDisplayListArray(SkinPart partData) {
        ArrayList<ColouredVertexWithUV> normalVertexList = new ArrayList<ColouredVertexWithUV>();
        ArrayList<ColouredVertexWithUV> glowingVertexList = new ArrayList<ColouredVertexWithUV>();
        float scale = 0.0625F;
        
        for (int i = 0; i < partData.getArmourData().size(); i++) {
            ICube cube = partData.getArmourData().get(i);
            
            ICubeColour cc = cube.getCubeColour();
            byte a = (byte) 255;
            if (cube.needsPostRender()) {
                a = (byte) 127;
            }
            
            if (cube.isGlowing()) {
                EquipmentPartRenderer.INSTANCE.main.buildDisplayListArray(glowingVertexList,
                        scale, cube.getFaceFlags(), cube.getX(), cube.getY(), cube.getZ(),
                        cc.getRed(), cc.getGreen(), cc.getBlue(), a);
            } else {
                EquipmentPartRenderer.INSTANCE.main.buildDisplayListArray(normalVertexList,
                        scale, cube.getFaceFlags(), cube.getX(), cube.getY(), cube.getZ(),
                        cc.getRed(), cc.getGreen(), cc.getBlue(), a);
            }
        }
        
        partData.getArmourData().clear();
        
        if (normalVertexList.size() > 0) {
            partData.normalVertexList = normalVertexList;
            partData.hasNormalBlocks = true;
        }
        
        if (glowingVertexList.size() > 0) {
            partData.glowingVertexList = glowingVertexList;
            partData.hasGlowingBlocks = true;
        }
    }
    
    /*
    private static void checkBlockFaceIntersectsBodyPart(EnumBodyPart bodyPart, ICube block) {
        ForgeDirection[] dirs = { ForgeDirection.EAST, ForgeDirection.WEST,  ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH };
        for (int i = 0; i < dirs.length; i++) {
            ForgeDirection dir = dirs[i];
            if (cordsIntersectsBodyPart(bodyPart, (block.getX() + dir.offsetX), -(block.getY() + dir.offsetY + 1), (block.getZ() + dir.offsetZ))) {
                block.getFaceFlags().set(i, true);
            }
        }
    }
    
    private static boolean cordsIntersectsBodyPart(EnumBodyPart bodyPart, int x, int y, int z) {
        int xCen = x + bodyPart.xOrigin;
        int yCen = y + 1 + bodyPart.yOrigin;
        int zCen = z + bodyPart.zOrigin;
        
        if (bodyPart.xSize > xCen & 0 <= xCen) {
            if (bodyPart.zSize > zCen & 0 <= zCen) {
                if (bodyPart.ySize > yCen & 0 <= yCen) {
                    return true;
                }
            }
        }
        return false;
    }
    */
}
