package riskyken.armourersWorkshop.common.items;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.lib.LibItemNames;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.tileentities.IWorldColourable;
import riskyken.armourersWorkshop.utils.UtilColour;
import riskyken.armourersWorkshop.utils.UtilItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemShadeNoiseTool extends AbstractModItem {

    public ItemShadeNoiseTool() {
        super(LibItemNames.SHADE_NOISE_TOOL);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibModInfo.ID + ":" + "shadeNoiseTool");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);

        if (!player.isSneaking() & block instanceof IWorldColourable) {
            if (!world.isRemote) {
                Color c = new Color(((IWorldColourable) block).getColour(world,
                        x, y, z));
                int intensity = UtilItems.getIntensityFromStack(stack, 16);
                ((IWorldColourable) block).setColour(world, x, y, z, UtilColour
                        .addShadeNoise(c, intensity).getRGB());
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
        int intensity = UtilItems.getIntensityFromStack(stack, 16);
        list.add("Intensity: " + intensity);
    }
}
