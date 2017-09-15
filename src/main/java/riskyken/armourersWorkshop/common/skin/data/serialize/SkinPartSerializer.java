package riskyken.armourersWorkshop.common.skin.data.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.Level;

import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.common.exception.InvalidCubeTypeException;
import riskyken.armourersWorkshop.common.skin.cubes.CubeMarkerData;
import riskyken.armourersWorkshop.common.skin.data.SkinCubeData;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;
import riskyken.armourersWorkshop.utils.ModLogger;
import riskyken.armourersWorkshop.utils.StreamUtils;

public final class SkinPartSerializer {
    
    private SkinPartSerializer() {}
    
    public static SkinPart loadSkinPart(DataInputStream stream, int version) throws IOException, InvalidCubeTypeException {
        ISkinPartType skinPart = null;
        SkinCubeData cubeData = null;
        ArrayList<CubeMarkerData> markerBlocks = null;
        if (version < 6) {
            skinPart = SkinTypeRegistry.INSTANCE.getSkinPartFromLegacyId(stream.readByte());
            if (skinPart == null) {
                ModLogger.log(Level.ERROR,"Skin part was null");
                throw new IOException("Skin part was null");
            }
        } else {
            String regName = null;
            if (version > 12) {
                regName = StreamUtils.readString(stream, Charsets.US_ASCII);
            } else {
                regName = stream.readUTF();
            }
            if (regName.equals("armourers:skirt.base")) {
                regName = "armourers:legs.skirt";
            }
            if (regName.equals("armourers:bow.base")) {
                regName = "armourers:bow.frame1";
            }
            if (regName.equals("armourers:arrow.base")) {
                regName = "armourers:bow.arrow";
            }
            skinPart = SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName(regName);
            
            if (skinPart == null) {
                ModLogger.log(Level.ERROR,"Skin part was null - reg name: " + regName);
                throw new IOException("Skin part was null - reg name: " + regName);
            }
        }
        
        cubeData = new SkinCubeData();
        cubeData.readFromStream(stream, version, skinPart);
        markerBlocks = new ArrayList<CubeMarkerData>();
        if (version > 8) {
            int markerCount = stream.readInt();
            for (int i = 0; i < markerCount; i++) {
                markerBlocks.add(new CubeMarkerData(stream, version));
            }
        }
        return new SkinPart(cubeData, skinPart, markerBlocks);
    }
    
    public static void saveSkinPart(SkinPart skinPart, DataOutputStream stream) throws IOException {
        StreamUtils.writeString(stream, Charsets.US_ASCII, skinPart.getPartType().getRegistryName());
        skinPart.getCubeData().writeToStream(stream);
        stream.writeInt(skinPart.getMarkerCount());
        for (int i = 0; i < skinPart.getMarkerCount(); i++) {
            skinPart.getMarkerBlocks().get(i).writeToStream(stream);
        }
    }
}
