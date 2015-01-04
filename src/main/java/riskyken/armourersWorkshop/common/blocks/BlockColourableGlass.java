package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.api.common.painting.IPantable;
import riskyken.armourersWorkshop.api.common.painting.IPantableBlock;
import riskyken.armourersWorkshop.client.render.block.RenderBlockGlowing;
import riskyken.armourersWorkshop.common.items.block.ModItemBlock;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.tileentities.TileEntityColourable;
import riskyken.armourersWorkshop.utils.UtilColour;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColourableGlass extends AbstractModBlock implements ITileEntityProvider, IPantableBlock {

    public BlockColourableGlass(String name, boolean glowing) {
        super(name);
        if (glowing) {
            setLightLevel(1.0F);
        }
        setHardness(1.0F);
    }

    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ModItemBlock.class, "block." + name);
        return super.setBlockName(name);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon(LibModInfo.ID + ":" + "colourableGlass");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass() {
        return 1;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        Block sideBlock = world.getBlock(x, y, z);
        if (sideBlock == this) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if (te != null & te instanceof TileEntityColourable) {
            return ((TileEntityColourable)te).getColour();
        }
        return super.colorMultiplier(blockAccess, x, y, z);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.dye) {
            if (world.isRemote) { return true; }
            this.setColour(world, x, y, z, UtilColour.getMinecraftColor(-player.getCurrentEquippedItem().getItemDamage() + 15));
            return true;
        }
        return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityColourable();
    }

    @Override
    public boolean setColour(World world, int x, int y, int z, int colour) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null & te instanceof IPantable) {
            ((IPantable)te).setColour(colour);
            return true;
        }
        return false;
    }

    @Override
    public int getColour(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null & te instanceof IPantable) {
            return ((IPantable)te).getColour();
        }
        return 0;
    }
    
    @Override
    public int getRenderType() {
        return RenderBlockGlowing.renderId;
    }
}
