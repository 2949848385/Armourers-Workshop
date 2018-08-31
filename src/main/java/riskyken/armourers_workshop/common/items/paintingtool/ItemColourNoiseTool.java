package riskyken.armourers_workshop.common.items.paintingtool;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import riskyken.armourers_workshop.api.common.painting.IPantableBlock;
import riskyken.armourers_workshop.common.blocks.BlockLocation;
import riskyken.armourers_workshop.common.blocks.ModBlocks;
import riskyken.armourers_workshop.common.items.AbstractModItem;
import riskyken.armourers_workshop.common.lib.LibItemNames;
import riskyken.armourers_workshop.common.network.PacketHandler;
import riskyken.armourers_workshop.common.network.messages.client.MessageClientToolPaintBlock;
import riskyken.armourers_workshop.common.painting.IBlockPainter;
import riskyken.armourers_workshop.common.painting.PaintType;
import riskyken.armourers_workshop.common.painting.tool.AbstractToolOption;
import riskyken.armourers_workshop.common.painting.tool.IConfigurableTool;
import riskyken.armourers_workshop.common.painting.tool.ToolOptions;
import riskyken.armourers_workshop.common.undo.UndoManager;
import riskyken.armourers_workshop.utils.UtilColour;
import riskyken.armourers_workshop.utils.UtilItems;

public class ItemColourNoiseTool extends AbstractModItem implements IConfigurableTool, IBlockPainter {

    public ItemColourNoiseTool() {
        super(LibItemNames.COLOUR_NOISE_TOOL);
        setSortPriority(15);
    }
/*
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);

        if (!player.isSneaking() & block instanceof IPantableBlock) {
            if (!world.isRemote) {
                UndoManager.begin(player);
            }
            
            if ((Boolean) ToolOptions.FULL_BLOCK_MODE.readFromNBT(stack.getTagCompound())) {
                for (int i = 0; i < 6; i++) {
                    usedOnBlockSide(stack, player, world, new BlockLocation(x, y, z), block, i);
                }
            } else {
                usedOnBlockSide(stack, player, world, new BlockLocation(x, y, z), block, side);
            }
            if (!world.isRemote) {
                UndoManager.end(player);
                world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, LibSounds.BURN, 1.0F, 1.0F);
            }
            return true;
        }
        
        if (block == ModBlocks.armourerBrain & player.isSneaking()) {
            if (!world.isRemote) {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityArmourer) {
                    ((TileEntityArmourer)te).toolUsedOnArmourer(this, world, stack, player);
                }
            }
            return true;
        }
        
        return false;
    }*/
    
    @SuppressWarnings("deprecation")
    @Override
    public void usedOnBlockSide(ItemStack stack, EntityPlayer player, World world, BlockLocation bl, Block block, int side) {
        int intensity = UtilItems.getIntensityFromStack(stack, 16);
        IPantableBlock worldColourable = (IPantableBlock) block;
        if (worldColourable.isRemoteOnly(world, bl.x, bl.y, bl.z, side) & world.isRemote) {
            byte[] rgbt = new byte[4];
            int oldColour = worldColourable.getColour(world, bl.x, bl.y, bl.z, side);
            PaintType oldPaintType = worldColourable.getPaintType(world, bl.x, bl.y, bl.z, side);
            Color c = UtilColour.addColourNoise(new Color(oldColour), intensity);
            rgbt[0] = (byte)c.getRed();
            rgbt[1] = (byte)c.getGreen();
            rgbt[2] = (byte)c.getBlue();
            rgbt[3] = (byte)oldPaintType.getKey();
            if (block == ModBlocks.boundingBox && oldPaintType == PaintType.NONE) {
                rgbt[3] = (byte)PaintType.NORMAL.getKey();
            }
            MessageClientToolPaintBlock message = new MessageClientToolPaintBlock(bl.x, bl.y, bl.z, (byte)side, rgbt);
            PacketHandler.networkWrapper.sendToServer(message);
        } else if(!worldColourable.isRemoteOnly(world, bl.x, bl.y, bl.z, side) & !world.isRemote) {
            int oldColour = worldColourable.getColour(world, bl.x, bl.y, bl.z, side);
            byte oldPaintType = (byte) worldColourable.getPaintType(world, bl.x, bl.y, bl.z, side).getKey();
            int newColour = UtilColour.addColourNoise(new Color(oldColour), intensity).getRGB();
            UndoManager.blockPainted(player, world, bl.x, bl.y, bl.z, oldColour, oldPaintType, side);
            ((IPantableBlock) block).setColour(world, bl.x, bl.y, bl.z, newColour, side);
        }
    }
    /*
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
        int intensity = UtilItems.getIntensityFromStack(stack, 16);
        String rollover = TranslateUtils.translate("item.armourersworkshop:rollover.intensity", intensity);
        list.add(rollover);
        list.add(TranslateUtils.translate("item.armourersworkshop:rollover.openSettings"));
    }
    */
    @Override
    public void getToolOptions(ArrayList<AbstractToolOption> toolOptionList) {
        toolOptionList.add(ToolOptions.FULL_BLOCK_MODE);
        toolOptionList.add(ToolOptions.INTENSITY);
    }
}
