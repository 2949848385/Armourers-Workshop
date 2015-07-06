package riskyken.armourersWorkshop.common.skin.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinPart;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.client.model.bake.ColouredVertexWithUV;
import riskyken.armourersWorkshop.common.exception.InvalidCubeTypeException;
import riskyken.armourersWorkshop.common.skin.cubes.CubeFactory;
import riskyken.armourersWorkshop.common.skin.cubes.ICube;
import riskyken.armourersWorkshop.common.skin.cubes.LegacyCubeHelper;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;

public class SkinPart implements ISkinPart {
    
    private static final String TAG_PART_NAME = "partName";
    private static final String TAG_CUBE_LIST = "cubeList";
    private static final String TAG_ID = "id";
    
    private ArrayList<ICube> armourData;
    private ISkinPartType skinPart;
    
    @SideOnly(Side.CLIENT)
    public ArrayList<ColouredVertexWithUV> normalVertexList;
    @SideOnly(Side.CLIENT)
    public ArrayList<ColouredVertexWithUV> glowingVertexList;
    
    public boolean hasNormalBlocks;
    public boolean hasGlowingBlocks;
    
    @SideOnly(Side.CLIENT)
    public int[] totalCubesInPart;
    @SideOnly(Side.CLIENT)
    public boolean displayNormalCompiled;
    @SideOnly(Side.CLIENT)
    public boolean displayGlowingCompiled;
    
    @SideOnly(Side.CLIENT)
    public int displayListNormal;
    @SideOnly(Side.CLIENT)
    public int displayListGlowing;

    @SideOnly(Side.CLIENT)
    public void cleanUpDisplayLists() {
        if (this.displayNormalCompiled) {
            if (hasNormalBlocks) {
                GLAllocation.deleteDisplayLists(this.displayListNormal);
            }
        }
        if (this.displayGlowingCompiled) {
            if (hasGlowingBlocks) {
                GLAllocation.deleteDisplayLists(this.displayListGlowing);  
            }
        }
    }
    
    public SkinPart(ArrayList armourData, ISkinPartType skinPart) {
        this.armourData = armourData;
        this.skinPart = skinPart;
    }
    
    public SkinPart(ISkinPartType skinPart) {
        this.armourData = new ArrayList<ICube>();
        this.skinPart = skinPart;
    }

    public SkinPart(DataInputStream stream, int version) throws IOException, InvalidCubeTypeException {
        readFromStream(stream, version);
    }

    @Override
    public ISkinPartType getPartType() {
        return this.skinPart;
    }

    public ArrayList<ICube> getArmourData() {
        return armourData;
    }
    
    public void writeToCompound(NBTTagCompound compound) {
        compound.setString(TAG_PART_NAME, skinPart.getRegistryName());
        NBTTagList cubeList = new NBTTagList();
        for (int i = 0; i < armourData.size(); i++) {
            NBTTagCompound cubeCompound = new NBTTagCompound();
            ICube cube = armourData.get(i);
            cube.writeToCompound(cubeCompound);
            cubeList.appendTag(cubeCompound);
        }
        compound.setTag(TAG_CUBE_LIST, cubeList);
    }
    
    public void readFromCompound(NBTTagCompound compound) throws InvalidCubeTypeException {
        String partName = compound.getString(TAG_PART_NAME);
        skinPart = SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName(partName);
        NBTTagList cubeList = compound.getTagList(TAG_CUBE_LIST, NBT.TAG_COMPOUND);
        for (int i = 0; i < cubeList.tagCount(); i++) {
            NBTTagCompound cubeCompound = cubeList.getCompoundTagAt(i);
            byte cubeId = cubeCompound.getByte(TAG_ID);
            ICube cube = CubeFactory.INSTANCE.getCubeInstanceFormId(cubeId);
            cube.readFromCompound(cubeCompound);
            armourData.add(cube);
        }
    }
    
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeUTF(skinPart.getRegistryName());
        stream.writeInt(armourData.size());
        for (int i = 0; i < armourData.size(); i++) {
            armourData.get(i).writeToStream(stream);
        }
    }
    
    private void readFromStream(DataInputStream stream, int version) throws IOException, InvalidCubeTypeException {
        if (version < 6) {
            skinPart = SkinTypeRegistry.INSTANCE.getSkinPartFromLegacyId(stream.readByte());
        } else {
            skinPart = SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName(stream.readUTF());
        }
        int size = stream.readInt();
        armourData = new ArrayList<ICube>();
        for (int i = 0; i < size; i++) {
            ICube cube;
            if (version < 3) {
                cube = LegacyCubeHelper.loadlegacyCube(stream, version, skinPart);
            } else {
                byte id = stream.readByte();
                cube = CubeFactory.INSTANCE.getCubeInstanceFormId(id);
                cube.readFromStream(stream, version, skinPart);
            }
            armourData.add(cube);
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < armourData.size(); i++) {
            result += armourData.get(i).toString();
        }
        return "CustomArmourPartData [armourData=" + armourData + "" + result;
    }
}
