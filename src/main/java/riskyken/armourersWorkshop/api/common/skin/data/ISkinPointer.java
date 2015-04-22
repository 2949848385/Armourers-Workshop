package riskyken.armourersWorkshop.api.common.skin.data;

import net.minecraft.nbt.NBTTagCompound;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;

public interface ISkinPointer {

    public int getSkinId();
    
    public ISkinType getSkinType();
    
    public void readFromCompound(NBTTagCompound compound);
    
    public void writeToCompound(NBTTagCompound compound);
}
