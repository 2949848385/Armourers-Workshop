package riskyken.armourersWorkshop.api.common.skin.data;

import riskyken.armourersWorkshop.api.common.library.ILibraryFile;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;

public interface ISkinIdentifier {
    
    public int getSkinLocalId();
    
    public ILibraryFile getSkinLibraryFile();
    
    public int getSkinGlobalId();
    
    public ISkinType getSkinType();
}
