package riskyken.armourersWorkshop.common.skin.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinIdentifier;
import riskyken.armourersWorkshop.common.library.LibraryFile;

public class SkinIdentifier implements ISkinIdentifier {
    
    private static final String TAG_SKIN_ID_DATA = "identifier";
    private static final String TAG_SKIN_LOCAL_ID = "localId";
    private static final String TAG_SKIN_LIBRARY_FILE = "libraryFile";
    private static final String TAG_SKIN_GLOBAL_ID = "globalId";
    
    private static final String TAG_SKIN_OLD_ID = "skinId";
    
    private int localId;
    private LibraryFile libraryFile;
    private int globalId;
    
    public SkinIdentifier(Skin skin) {
        this.localId = skin.lightHash();
    }
    
    public SkinIdentifier(int localId, LibraryFile libraryFile, int globalId) {
        this.localId = localId;
        this.libraryFile = libraryFile;
        this.globalId = globalId;
    }
    
    public SkinIdentifier(ISkinIdentifier identifier) {
        this(identifier.getSkinLocalId(), identifier.getSkinLibraryFile(), identifier.getSkinGlobalId());
    }
    
    @Override
    public int getSkinLocalId() {
        return localId;
    }

    @Override
    public LibraryFile getSkinLibraryFile() {
        return libraryFile;
    }

    @Override
    public int getSkinGlobalId() {
        return globalId;
    }

    @Override
    public String toString() {
        return "SkinIdentifier [localId=" + localId + ", libraryFile=" + libraryFile + ", globalId=" + globalId + "]";
    }
    
    public void readFromCompound(NBTTagCompound compound) {
        if (compound.hasKey(TAG_SKIN_OLD_ID, NBT.TAG_INT)) {
            localId = compound.getInteger(TAG_SKIN_OLD_ID);
        } else {
            NBTTagCompound idDataCompound = compound.getCompoundTag(TAG_SKIN_ID_DATA);
            localId = idDataCompound.getInteger(TAG_SKIN_LOCAL_ID);
            // TODO Read library file.
            globalId = idDataCompound.getInteger(TAG_SKIN_GLOBAL_ID);
        }
    }
    
    public void writeToCompound(NBTTagCompound compound) {
        NBTTagCompound idDataCompound = new NBTTagCompound();
        idDataCompound.setInteger(TAG_SKIN_LOCAL_ID, localId);
        // TODO Write library file.
        idDataCompound.setInteger(TAG_SKIN_GLOBAL_ID, globalId);
        compound.setTag(TAG_SKIN_ID_DATA, idDataCompound);
    }
}
