package riskyken.armourersWorkshop.common.items;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.lib.LibItemNames;

public class ItemDebugTool extends AbstractModItem {

    public ItemDebugTool() {
        super(LibItemNames.DEBUG_TOOL, true);
        setSortPriority(-1);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            player.openGui(ArmourersWorkshop.instance, LibGuiIds.DEBUG_TOOL, world, 0, 0, 0);
        }
        return stack;
    }
    
    public static interface IDebug {   
        public void getDebugHoverText(World world, int x, int y, int z, ArrayList<String> textLines);
    }
}
