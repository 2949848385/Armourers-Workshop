package riskyken.armourersWorkshop.common.undo;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.api.common.painting.IPantableBlock;

public class PlayerUndoData {
    
    private ArrayList<UndoData> undos;
    private EntityPlayer player;
    
    public PlayerUndoData(EntityPlayer player) {
        this.player = player;
        undos = new ArrayList<UndoData>();
    }

    public void addUndoData(UndoData undoData) {
        undos.add(undoData);
        if (undos.size() > UndoManager.maxUndos) {
            undos.remove(0);
        }
    }

    public void playerPressedUndo(World world) {
        if (undos.size() < 1) {
            return;
        }
        
        UndoData undoData = undos.get(undos.size() - 1);
        if (world.provider.dimensionId != undoData.dimensionId) {
            return;
        }
        
        Block block = world.getBlock(undoData.blockX, undoData.blockY, undoData.blockZ);
        if (block instanceof IPantableBlock) {
            IPantableBlock worldColourable = (IPantableBlock) block;
            worldColourable.setColour(world, undoData.blockX, undoData.blockY, undoData.blockZ, undoData.colour);
        }
        undos.remove(undos.size() - 1);
    }
    
    public int getAvalableUndos() {
        return undos.size();
    }
}
