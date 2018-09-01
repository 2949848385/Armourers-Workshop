package moe.plushie.armourers_workshop.common.tileentities;

import moe.plushie.armourers_workshop.common.network.messages.client.MessageClientGuiButton.IButtonPress;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGlobalSkinLibrary extends TileEntity implements IButtonPress  {
    
    public TileEntityGlobalSkinLibrary() {
    }

    @Override
    public void buttonPressed(byte buttonId) {
        if (buttonId == 0) {
            
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos());
    }
}
