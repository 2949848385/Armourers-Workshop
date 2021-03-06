package riskyken.armourersWorkshop.common.skin.data;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import riskyken.armourersWorkshop.api.common.IRectangle3D;
import riskyken.armourersWorkshop.api.common.skin.Rectangle3D;
import riskyken.armourersWorkshop.api.common.skin.data.ISkin;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinPart;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.client.skin.SkinModelTexture;
import riskyken.armourersWorkshop.common.skin.cubes.CubeRegistry;
import riskyken.armourersWorkshop.common.skin.cubes.ICube;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;

public class Skin implements ISkin {
    
    public static final int FILE_VERSION = 13;
    
    public static final String KEY_AUTHOR_NAME = "authorName";
    public static final String KEY_AUTHOR_UUID = "authorUUID";
    public static final String KEY_CUSTOM_NAME = "customName";
    public static final String KEY_FILE_NAME = "fileName";
    
    private SkinProperties properties;
    private ISkinType equipmentSkinType;
    private int[] paintData;
    private ArrayList<SkinPart> parts;
    private int lightHash = 0;
    
    public SkinIdentifier requestId;
    public int serverId = -1;
    
    @SideOnly(Side.CLIENT)
    public SkinModelTexture skinModelTexture;
    
    
    @SideOnly(Side.CLIENT)
    public int paintTextureId;
    
    private int[] averageR = new int[10];
    private int[] averageG = new int[10];
    private int[] averageB = new int[10];
    
    public void setAverageDyeValues(int[] r, int[] g, int[] b) {
        this.averageR = r;
        this.averageG = g;
        this.averageB = b;
    }
    
    @SideOnly(Side.CLIENT)
    public Rectangle3D getSkinBounds() {
        int x = 0;
        int y = 0;
        int z = 0;
        
        int width = 1;
        int height = 1;
        int depth = 1;
        
        for (int i = 0; i < getPartCount(); i++) {
            if (!(getSkinType() == SkinTypeRegistry.skinBow && i > 0)) {
                
                SkinPart skinPart = getParts().get(i);
                Rectangle3D bounds = skinPart.getPartBounds();
                
                width = Math.max(width, bounds.getWidth());
                height = Math.max(height, bounds.getHeight());
                depth = Math.max(depth, bounds.getDepth());
                
                x = bounds.getX();
                y = bounds.getY();
                z = bounds.getZ();
                
                if (hasPaintData()) {
                    IRectangle3D skinRec = skinPart.getPartType().getGuideSpace();
                    
                    width = Math.max(width, skinRec.getWidth());
                    height = Math.max(height, skinRec.getHeight());
                    depth = Math.max(depth, skinRec.getDepth());
                    
                    x = Math.max(x, skinRec.getX());
                    y = Math.max(y, skinRec.getY());
                    z = Math.max(z, skinRec.getZ());
                }

            }
        }
        
        if (getPartCount() == 0) {
            for (int i = 0; i < getSkinType().getSkinParts().size(); i++) {
                ISkinPartType part = getSkinType().getSkinParts().get(i);
                
                IRectangle3D skinRec = part.getGuideSpace();
                
                width = Math.max(width, skinRec.getWidth());
                height = Math.max(height, skinRec.getHeight());
                depth = Math.max(depth, skinRec.getDepth());
                
                x = Math.min(x, skinRec.getX());
                y = Math.max(y, skinRec.getY());
                z = Math.min(z, skinRec.getZ());
            }
        }
        
        return new Rectangle3D(x, y, z, width, height, depth);
    }
    
    public int[] getAverageDyeColour(int dyeNumber) {
        return new int[] { averageR[dyeNumber], averageG[dyeNumber], averageB[dyeNumber] };
    }
    
    public SkinProperties getProperties() {
        return properties;
    }
    
    public Skin(SkinProperties properties, ISkinType equipmentSkinType, int[] paintData, ArrayList<SkinPart> equipmentSkinParts) {
        this.properties = properties;
        this.equipmentSkinType = equipmentSkinType;
        this.paintData = null;
        //Check if the paint data has any paint on it.
        if (paintData != null) {
            boolean validPaintData = false;
            for (int i = 0; i < SkinTexture.TEXTURE_SIZE; i++) {
                if (paintData[i] >>> 16 != 255) {
                    validPaintData = true;
                    break;
                }
            }
            if (validPaintData) {
                this.paintData = paintData;
            }
        }
        
        this.parts = equipmentSkinParts;
    }

    @SideOnly(Side.CLIENT)
    public void cleanUpDisplayLists() {
        for (int i = 0; i < parts.size(); i++) {
            parts.get(i).getClientSkinPartData().cleanUpDisplayLists();
        }
        if (hasPaintData()) {
            skinModelTexture.deleteGlTexture();
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void blindPaintTexture() {
        if (hasPaintData()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, paintTextureId);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public int getModelCount() {
        int count = 0;
            for (int i = 0; i < parts.size(); i++) {
                count += parts.get(i).getModelCount();
            }
        return count;
    }
    
    public int getPartCount() {
        return parts.size();
    }
    
    public int lightHash() {
        if (lightHash == 0) {
            lightHash = this.hashCode();
        }
        return lightHash;
    }
    
    @Override
    public ISkinType getSkinType() {
        return equipmentSkinType;
    }
    
    public boolean hasPaintData() {
        return paintData != null;
    }
    
    public int[] getPaintData() {
        return paintData;
    }
    
    public ArrayList<SkinPart> getParts() {
        return parts;
    }
    
    public SkinPart getSkinPartFromType(ISkinPartType skinPartType) {
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getPartType() == skinPartType) {
                return parts.get(i);
            }
        }
        return null;
    }
    
    @Override
    public ArrayList<ISkinPart> getSubParts() {
        ArrayList<ISkinPart> partList = new ArrayList<ISkinPart>();
        for (int i = 0; i < parts.size(); i++) {
            partList.add(parts.get(i));
        }
        return partList;
    }
    
    public boolean hasPart(String partRegistryName) {
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getPartType().getRegistryName().equals(partRegistryName)) {
                return true;
            }
        }
        return false;
    }
    
    public SkinPart getPart(String partRegistryName) {
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getPartType().getRegistryName().equals(partRegistryName)) {
                return parts.get(i);
            }
        }
        return null;
    }
    
    public String getCustomName() {
        return properties.getPropertyString(KEY_CUSTOM_NAME, "");
    }
    
    public String getAuthorName() {
        return properties.getPropertyString(KEY_AUTHOR_NAME, "");
    }
    
    public int getTotalCubes() {
        int totalCubes = 0;
        for (int i = 0; i < CubeRegistry.INSTANCE.getTotalCubes(); i++) {
            ICube cube = CubeRegistry.INSTANCE.getCubeFormId((byte) i);
            totalCubes += getTotalOfCubeType(cube);
        }
        return totalCubes;
    }
    
    public int getTotalOfCubeType(ICube cube) {
        int totalOfCube = 0;
        int cubeId = cube.getId();
        for (int i = 0; i < parts.size(); i++) {
            totalOfCube += parts.get(i).getClientSkinPartData().totalCubesInPart[cubeId];
        }
        return totalOfCube;
    }

    @Override
    public int hashCode() {
        if (lightHash == 0) {
            String result = this.toString();
            for (int i = 0; i < parts.size(); i++) {
                result += parts.get(i).toString();
            }
            lightHash = result.hashCode();
        }
        return lightHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Skin other = (Skin) obj;
        if (other.lightHash == lightHash)
            return true;
        return false;
    }

    @Override
    public String toString() {
        String returnString = "Skin [properties=" + properties + ", type=" + equipmentSkinType.getName().toUpperCase();
        if (this.paintData != null) {
            returnString += ", paintData=" + Arrays.hashCode(paintData);
        }
        returnString += "]";
        return returnString;
    }

    public int getMarkerCount() {
        int count = 0;
        for (int i = 0; i < parts.size(); i++) {
            count += parts.get(i).getMarkerBlocks().size();
        }
        return count;
    }
}
