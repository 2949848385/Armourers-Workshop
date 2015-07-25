package riskyken.armourersWorkshop.common.blocks;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.common.lib.LibBlockNames;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.tileentities.TileEntityBoundingBox;

public class BlockBoundingBox extends Block implements ITileEntityProvider/*, IPantableBlock*/ {

    protected BlockBoundingBox() {
        super(Material.cloth);
        //setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        setBlockUnbreakable();
        setStepSound(soundTypeCloth);
        setBlockName(LibBlockNames.BOUNDING_BOX);
        setLightOpacity(0);
    }
    
    @Override
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        world.setBlockToAir(x, y, z);
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return null;
    }
    
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityBoundingBox) {
            if (((TileEntityBoundingBox)tileEntity).isParentValid()) {
                return false;
            } else {
                world.setBlockToAir(x, y, z);
                return true;
            }
        }
        world.setBlockToAir(x, y, z);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon(LibModInfo.ID + ":" + "colourable");
    }
    
    @Override
    public int getRenderType() {
        return -1;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, "block." + name);
        return super.setBlockName(name);
    }
    
    @Override
    public String getUnlocalizedName() {
        return getModdedUnlocalizedName(super.getUnlocalizedName());
    }

    protected String getModdedUnlocalizedName(String unlocalizedName) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityBoundingBox();
    }
    /*
    @Override
    public boolean setColour(IBlockAccess world, int x, int y, int z, int colour, int side) {
        ForgeDirection sideBlock = ForgeDirection.getOrientation(side);
        if (world.getBlock(x + sideBlock.offsetX, y + sideBlock.offsetY, z + sideBlock.offsetZ) == this) {
            return false;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityBoundingBox) {
            TileEntityArmourerBrain parent = ((TileEntityBoundingBox)te).getParent();
            if (parent != null) {
                ISkinType skinType = parent.getSkinType();
                Point texturePoint = SkinTextureHelper.getTextureLocationFromWorldBlock((TileEntityBoundingBox)te, side);
                parent.updatePaintData(texturePoint.x, texturePoint.y, colour);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getColour(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection sideBlock = ForgeDirection.getOrientation(side);
        if (world.getBlock(x + sideBlock.offsetX, y + sideBlock.offsetY, z + sideBlock.offsetZ) == this) {
            return 0x00FFFFFF;
        }
        
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityBoundingBox) {
            TileEntityArmourerBrain parent = ((TileEntityBoundingBox)te).getParent();
            if (parent != null) {
                ISkinType skinType = parent.getSkinType();
                Point texturePoint = SkinTextureHelper.getTextureLocationFromWorldBlock((TileEntityBoundingBox)te, side);
                int colour = parent.getPaintData(texturePoint.x, texturePoint.y);
                if (colour >>> 16 == 255) {
                    GameProfile gameProfile = parent.getGameProfile();
                    BufferedImage playerSkin = SkinHelper.getBufferedImageSkin(gameProfile);
                    colour = playerSkin.getRGB(texturePoint.x, texturePoint.y);
                }
                return colour;
            }
        }
        
        return 0x00FFFFFF;
    }

    @Override
    public ICubeColour getColour(IBlockAccess world, int x, int y, int z) {
        return null;
    }
    */
}
