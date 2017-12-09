package riskyken.armourersWorkshop.common.skin.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import riskyken.armourersWorkshop.api.common.library.ILibraryFile;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinIdentifier;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.common.library.LibraryFile;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;

public class SkinIdentifier implements ISkinIdentifier {
    
    private static final String TAG_SKIN_ID_DATA = "identifier";
    private static final String TAG_SKIN_LOCAL_ID = "localId";
    private static final String TAG_SKIN_LIBRARY_FILE = "libraryFile";
    private static final String TAG_SKIN_GLOBAL_ID = "globalId";
    private static final String TAG_SKIN_TYPE = "skinType";
    
    private static final String TAG_SKIN_OLD_ID = "skinId";
    
    private int localId;
    private ILibraryFile libraryFile;
    private int globalId;
    private ISkinType skinType;
    
    
    // 1 2 3
    // 1 3 2
    // 2 1 3
    // 2 3 1
    // 3 1 2
    // 3 2 1
    
    // 6 Permutations
    // primary secondary tertiary
    
    public SkinIdentifier(int localId, ILibraryFile libraryFile, int globalId, ISkinType skinType) {
        this.localId = localId;
        this.libraryFile = libraryFile;
        this.globalId = globalId;
        this.skinType = skinType;
    }
    
    public SkinIdentifier(Skin skin) {
        this(skin.lightHash(), null, 0, skin.getSkinType());
    }
    
    public SkinIdentifier(ISkinIdentifier identifier) {
        this(identifier.getSkinLocalId(), identifier.getSkinLibraryFile(), identifier.getSkinGlobalId(), identifier.getSkinType());
    }
    
    public boolean hasLocalId() {
        return localId != 0;
    }
    
    public boolean hasLibraryFile() {
        return libraryFile != null;
    }
    
    public boolean hasGlobalId() {
        return globalId != 0;
    }
    
    @Override
    public int getSkinLocalId() {
        return localId;
    }

    @Override
    public ILibraryFile getSkinLibraryFile() {
        return libraryFile;
    }

    @Override
    public int getSkinGlobalId() {
        return globalId;
    }
    
    @Override
    public ISkinType getSkinType() {
        return skinType;
    }

    @Override
    public String toString() {
        return "SkinIdentifier [localId=" + localId + ", libraryFile=" + libraryFile + ", globalId=" + globalId + "]";
    }
    
    public void readFromCompound(NBTTagCompound compound) {
        NBTTagCompound idDataCompound = compound.getCompoundTag(TAG_SKIN_ID_DATA);
        localId = idDataCompound.getInteger(TAG_SKIN_LOCAL_ID);
        if (idDataCompound.hasKey(TAG_SKIN_LIBRARY_FILE, NBT.TAG_STRING)) {
            libraryFile = new LibraryFile(idDataCompound.getString(TAG_SKIN_LIBRARY_FILE));
        }
        globalId = idDataCompound.getInteger(TAG_SKIN_GLOBAL_ID);
        skinType = SkinTypeRegistry.INSTANCE.getSkinTypeFromRegistryName(idDataCompound.getString(TAG_SKIN_TYPE));
        
        if (compound.hasKey(TAG_SKIN_OLD_ID, NBT.TAG_INT)) {
            localId = compound.getInteger(TAG_SKIN_OLD_ID);
        }
        if (compound.hasKey(TAG_SKIN_TYPE, NBT.TAG_STRING)) {
            skinType = SkinTypeRegistry.INSTANCE.getSkinTypeFromRegistryName(compound.getString(TAG_SKIN_TYPE));
        }
    }
    
    public void writeToCompound(NBTTagCompound compound) {
        NBTTagCompound idDataCompound = new NBTTagCompound();
        idDataCompound.setInteger(TAG_SKIN_LOCAL_ID, localId);
        if (libraryFile != null) {
            idDataCompound.setString(TAG_SKIN_LIBRARY_FILE, libraryFile.getFullName());
        }
        idDataCompound.setInteger(TAG_SKIN_GLOBAL_ID, globalId);
        if (skinType != null) {
            idDataCompound.setString(TAG_SKIN_TYPE, skinType.getRegistryName());
        }
        compound.setTag(TAG_SKIN_ID_DATA, idDataCompound);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + globalId;
        result = prime * result + ((libraryFile == null) ? 0 : libraryFile.hashCode());
        result = prime * result + localId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SkinIdentifier other = (SkinIdentifier) obj;
        if (globalId != other.globalId)
            return false;
        if (libraryFile == null) {
            if (other.libraryFile != null)
                return false;
        } else if (!libraryFile.equals(other.libraryFile))
            return false;
        if (localId != other.localId)
            return false;
        return true;
    }
}
