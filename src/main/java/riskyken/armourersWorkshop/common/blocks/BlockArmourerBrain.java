package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.client.lib.LibBlockResources;
import riskyken.armourersWorkshop.common.items.block.ModItemBlock;
import riskyken.armourersWorkshop.common.lib.LibBlockNames;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.tileentities.TileEntityArmourerBrain;
import riskyken.armourersWorkshop.utils.UtilBlocks;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockArmourerBrain extends AbstractModBlock implements ITileEntityProvider {

    public BlockArmourerBrain() {
        super(LibBlockNames.ARMOURER_BRAIN);
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            TileEntity te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityArmourerBrain) {
                ForgeDirection direction = ForgeDirection.getOrientation(UtilBlocks.determineOrientationSide(world, x, y, z, entity));
                ((TileEntityArmourerBrain)te).setDirection(ForgeDirection.NORTH);
                if (!world.isRemote) {
                    ((TileEntityArmourerBrain)te).setGameProfile(player.getGameProfile());
                    ((TileEntityArmourerBrain)te).onPlaced();
                }
            }
        }
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        UtilBlocks.dropInventoryBlocks(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }
    
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null & te instanceof TileEntityArmourerBrain) {
            ((TileEntityArmourerBrain)te).preRemove();
        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ModItemBlock.class, "block." + name);
        return super.setBlockName(name);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
        if (!player.canPlayerEdit(x, y, z, side, player.getCurrentEquippedItem())) {
            return false;
        }
        if (!world.isRemote) {
            FMLNetworkHandler.openGui(player, ArmourersWorkshop.instance, LibGuiIds.ARMOURER, world, x, y, z);
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon(LibBlockResources.ARMOURER_TOP_BOTTOM);
        sideIcon = register.registerIcon(LibBlockResources.ARMOURER_SIDE);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (side < 2) {
            return blockIcon;
        }
        return sideIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityArmourerBrain();
    }
}
