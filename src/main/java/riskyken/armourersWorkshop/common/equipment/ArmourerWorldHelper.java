package riskyken.armourersWorkshop.common.equipment;

import java.util.ArrayList;

import javax.vecmath.Point3i;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import riskyken.armourersWorkshop.api.common.equipment.skin.IEquipmentSkinPart;
import riskyken.armourersWorkshop.api.common.equipment.skin.IEquipmentSkinType;
import riskyken.armourersWorkshop.common.Rectangle3D;
import riskyken.armourersWorkshop.common.blocks.ModBlocks;
import riskyken.armourersWorkshop.common.equipment.cubes.CubeRegistry;
import riskyken.armourersWorkshop.common.equipment.cubes.ICube;
import riskyken.armourersWorkshop.common.equipment.data.EquipmentSkinTypeData;
import riskyken.armourersWorkshop.common.equipment.data.EquipmentSkinPartData;
import riskyken.armourersWorkshop.common.equipment.data.InvalidCubeTypeException;
import riskyken.armourersWorkshop.common.tileentities.TileEntityBoundingBox;
import riskyken.armourersWorkshop.common.tileentities.TileEntityColourable;
import riskyken.armourersWorkshop.utils.UtilBlocks;

public final class ArmourerWorldHelper {
    
    public static EquipmentSkinTypeData saveSkinFromWorld(World world, IEquipmentSkinType skinType,
            String authorName, String customName, String tags,
            int xCoord, int yCoord, int zCoord, ForgeDirection direction) throws InvalidCubeTypeException {
        
        ArrayList<EquipmentSkinPartData> parts = new ArrayList<EquipmentSkinPartData>();
        
        
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            saveArmourPart(world, parts, skinType.getSkinParts().get(i), xCoord, yCoord, zCoord, direction);
        }
        
        if (parts.size() > 0) {
            return new EquipmentSkinTypeData(authorName, customName, tags, skinType, parts);
        } else {
            return null;
        }
    }
    
    private static void saveArmourPart(World world, ArrayList<EquipmentSkinPartData> armourData,
            IEquipmentSkinPart skinPart, int xCoord, int yCoord, int zCoord, ForgeDirection direction) throws InvalidCubeTypeException {
        ArrayList<ICube> armourBlockData = new ArrayList<ICube>();
        
        Rectangle3D buildSpace = skinPart.getBuildingSpace();
        Point3i offset = skinPart.getOffset();
        
        for (int ix = 0; ix < buildSpace.width; ix++) {
            for (int iy = 0; iy < buildSpace.height; iy++) {
                for (int iz = 0; iz < buildSpace.depth; iz++) {
                    
                    int x = xCoord + ix + -offset.x + buildSpace.x;
                    int y = yCoord + iy + -offset.y;
                    int z = zCoord + iz + -offset.z + buildSpace.z;
                    
                    int xOrigin = -ix + -buildSpace.x;
                    int yOrigin = -iy + -buildSpace.y;
                    int zOrigin = -iz + -buildSpace.z;
                    
                    saveArmourBlockToList(world, x, y, z,
                            xOrigin - 1,
                            yOrigin - 1,
                            -zOrigin,
                            armourBlockData, direction);
                }
            }
        }
        
        if (armourBlockData.size() > 0) {
            armourData.add(new EquipmentSkinPartData(armourBlockData, skinPart));
        }
    }
    
    private static void saveArmourBlockToList(World world, int x, int y, int z, int ix, int iy, int iz,
            ArrayList<ICube> list, ForgeDirection direction) throws InvalidCubeTypeException {
        if (world.isAirBlock(x, y, z)) {
            return;
        }
        
        Block block = world.getBlock(x, y, z);
        if (block == ModBlocks.colourable | block == ModBlocks.colourableGlowing | block == ModBlocks.colourableGlass | block == ModBlocks.colourableGlassGlowing) {
            int colour = UtilBlocks.getColourFromTileEntity(world ,x, y, z);
            byte blockType = 0;
            if (block == ModBlocks.colourableGlowing) {
                blockType = 1;
            }
            if (block == ModBlocks.colourableGlass) {
                blockType = 2;
            }
            if (block == ModBlocks.colourableGlassGlowing) {
                blockType = 3;
            }
            ICube blockData = CubeRegistry.INSTANCE.getCubeInstanceFormId(blockType);
            
            blockData.setX((byte) ix);
            blockData.setY((byte) iy);
            blockData.setZ((byte) iz);
            blockData.setColour(colour);
            
            list.add(blockData);
        }
    }
    
    public static void loadSkinIntoWorld(World world, int x, int y, int z, EquipmentSkinTypeData armourData, ForgeDirection direction) {
        ArrayList<EquipmentSkinPartData> parts = armourData.getParts();
        
        for (int i = 0; i < parts.size(); i++) {
            loadSkinPartIntoWorld(world, parts.get(i), x, y, z, direction);
        }
    }
    
    private static void loadSkinPartIntoWorld(World world, EquipmentSkinPartData partData, int xCoord, int yCoord, int zCoord, ForgeDirection direction) {
        IEquipmentSkinPart skinPart = partData.getSkinPart();
        Rectangle3D buildSpace = skinPart.getBuildingSpace();
        Point3i offset = skinPart.getOffset();
        
        for (int i = 0; i < partData.getArmourData().size(); i++) {
            ICube blockData = partData.getArmourData().get(i);
            
            int xOrigin = -offset.x;
            int yOrigin = -offset.y + -buildSpace.y;
            int zOrigin = -offset.z;
            
            loadSkinBlockIntoWorld(world, xCoord, yCoord, zCoord, xOrigin, yOrigin, zOrigin, blockData, direction);
        }
    }
    
    private static void loadSkinBlockIntoWorld(World world, int x, int y, int z, int xOrigin, int yOrigin, int zOrigin,
            ICube blockData, ForgeDirection direction) {
        
        int shiftX = -blockData.getX() - 1;
        int shiftY = blockData.getY() + 1;
        int shiftZ = blockData.getZ();
        
        int targetX = x + shiftX + xOrigin;
        int targetY = y + yOrigin - shiftY;
        int targetZ = z + shiftZ + zOrigin;
        
        if (world.isAirBlock(targetX, targetY, targetZ)) {
            Block targetBlock = ModBlocks.colourable;
            if (blockData.getId() == 1) {
                targetBlock = ModBlocks.colourableGlowing;
            }
            if (blockData.getId() == 2) {
                targetBlock = ModBlocks.colourableGlass;
            }
            if (blockData.getId() == 3) {
                targetBlock = ModBlocks.colourableGlassGlowing;
            }
            world.setBlock(targetX, targetY, targetZ, targetBlock);
            TileEntity te = world.getTileEntity(targetX, targetY, targetZ);
            if (te != null && te instanceof TileEntityColourable) {
                ((TileEntityColourable)te).setColour(blockData.getColour());
            }
        }
    }
    
    public static void createBoundingBoxes(World world, int x, int y, int z, int parentX, int parentY, int parentZ, IEquipmentSkinType skinType) {
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            IEquipmentSkinPart skinPart = skinType.getSkinParts().get(i);
            createBoundingBoxesForSkinPart(world, x, y, z, parentX, parentY, parentZ, skinPart);
        }
    }
    
    private static void createBoundingBoxesForSkinPart(World world, int x, int y, int z, int parentX, int parentY, int parentZ, IEquipmentSkinPart skinPart) {
        Rectangle3D buildSpace = skinPart.getBuildingSpace();
        Rectangle3D guideSpace = skinPart.getGuideSpace();
        Point3i offset = skinPart.getOffset();
        
        if (guideSpace == null) {
            return;
        }
        
        for (int ix = 0; ix < guideSpace.width; ix++) {
            for (int iy = 0; iy < guideSpace.height; iy++) {
                for (int iz = 0; iz < guideSpace.depth; iz++) {
                    int xTar = x + ix + -offset.x + guideSpace.x;
                    int yTar = y + iy + -offset.y + guideSpace.y - buildSpace.y;
                    int zTar = z + iz + -offset.z + guideSpace.z;
                    
                    if (world.isAirBlock(xTar, yTar, zTar)) {
                        world.setBlock(xTar, yTar, zTar, ModBlocks.boundingBox);
                        TileEntity te = null;
                        te = world.getTileEntity(xTar, yTar, zTar);
                        if (te != null && te instanceof TileEntityBoundingBox) {
                            ((TileEntityBoundingBox)te).setParent(parentX, parentY, parentZ, skinPart);
                        } else {
                            te = new TileEntityBoundingBox(parentX, parentY, parentZ, skinPart);
                            world.setTileEntity(xTar, yTar, zTar, te);
                        }
                    }
                    
                }
            }
        }
    }
    
    public static void removeBoundingBoxes(World world, int x, int y, int z, IEquipmentSkinType skinType) {
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            IEquipmentSkinPart skinPart = skinType.getSkinParts().get(i);
            removeBoundingBoxesForSkinPart(world, x, y, z, skinPart);
        }
    }
    
    private static void removeBoundingBoxesForSkinPart(World world, int x, int y, int z, IEquipmentSkinPart skinPart) {
        Rectangle3D buildSpace = skinPart.getBuildingSpace();
        Rectangle3D guideSpace = skinPart.getGuideSpace();
        Point3i offset = skinPart.getOffset();
        
        if (guideSpace == null) {
            return;
        }
        
        for (int ix = 0; ix < guideSpace.width; ix++) {
            for (int iy = 0; iy < guideSpace.height; iy++) {
                for (int iz = 0; iz < guideSpace.depth; iz++) {
                    int xTar = x + ix + -offset.x + guideSpace.x;
                    int yTar = y + iy + -offset.y + guideSpace.y - buildSpace.y;
                    int zTar = z + iz + -offset.z + guideSpace.z;
                    
                    if (world.blockExists(xTar, yTar, zTar)) {
                        if (world.getBlock(xTar, yTar, zTar) == ModBlocks.boundingBox) {
                            world.setBlockToAir(xTar, yTar, zTar);
                        }
                    }
                    
                }
            }
        }
    }
    
    public static int clearEquipmentCubes(World world, int x, int y, int z, IEquipmentSkinType skinType) {
        int blockCount = 0;
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            IEquipmentSkinPart skinPart = skinType.getSkinParts().get(i);
            blockCount += clearEquipmentCubesForSkinPart(world, x, y, z, skinPart);
        }
        return blockCount;
    }
    
    private static int clearEquipmentCubesForSkinPart(World world, int x, int y, int z, IEquipmentSkinPart skinPart) {
        Rectangle3D buildSpace = skinPart.getBuildingSpace();
        Point3i offset = skinPart.getOffset();
        int blockCount = 0;
        
        for (int ix = 0; ix < buildSpace.width; ix++) {
            for (int iy = 0; iy < buildSpace.height; iy++) {
                for (int iz = 0; iz < buildSpace.depth; iz++) {
                    int xTar = x + ix + -offset.x + buildSpace.x;
                    int yTar = y + iy + -offset.y;
                    int zTar = z + iz + -offset.z + buildSpace.z;
                    
                    if (world.blockExists(xTar, yTar, zTar)) {
                        Block block = world.getBlock(xTar, yTar, zTar);
                        if (
                            block == ModBlocks.colourable |
                            block == ModBlocks.colourableGlowing |
                            block == ModBlocks.colourableGlass |
                            block == ModBlocks.colourableGlassGlowing
                            ) {
                            world.setBlockToAir(xTar, yTar, zTar);
                            blockCount++;
                        }
                    }
                    
                }
            }
        }
        return blockCount;
    }
}
