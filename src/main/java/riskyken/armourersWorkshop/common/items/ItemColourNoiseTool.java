package riskyken.armourersWorkshop.common.items;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.api.common.painting.IPantableBlock;
import riskyken.armourersWorkshop.client.lib.LibItemResources;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.lib.LibItemNames;
import riskyken.armourersWorkshop.common.undo.UndoManager;
import riskyken.armourersWorkshop.utils.UtilColour;
import riskyken.armourersWorkshop.utils.UtilItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemColourNoiseTool extends AbstractModItem {

    public ItemColourNoiseTool() {
        super(LibItemNames.COLOUR_NOISE_TOOL);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibItemResources.COLOUR_NOISE_TOOL);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);

        if (!player.isSneaking() & block instanceof IPantableBlock) {
            if (!world.isRemote) {
                int intensity = UtilItems.getIntensityFromStack(stack, 16);
                IPantableBlock worldColourable = (IPantableBlock) block;
                int oldColour = worldColourable.getColour(world, x, y, z, side);
                int newColour = UtilColour.addColourNoise(new Color(oldColour), intensity).getRGB();
                UndoManager.playerPaintedBlock(player, world, x, y, z, oldColour);
                ((IPantableBlock) block).setColour(world, x, y, z, newColour, side);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote & player.isSneaking()) {
            player.openGui(ArmourersWorkshop.instance, LibGuiIds.TOOL_OPTIONS, world, 0, 0, 0);
        }
        return stack;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        super.addInformation(stack, player, list, p_77624_4_);
        String cGray = EnumChatFormatting.GRAY.toString();
        String cGold = EnumChatFormatting.GOLD.toString();
        int intensity = UtilItems.getIntensityFromStack(stack, 16);
        list.add(cGold + "Intensity: "+ cGray + intensity);
    }
}
