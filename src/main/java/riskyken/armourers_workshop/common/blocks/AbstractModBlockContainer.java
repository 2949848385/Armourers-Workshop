package riskyken.armourers_workshop.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import riskyken.armourers_workshop.ArmourersWorkshop;
import riskyken.armourers_workshop.common.creativetab.ISortOrder;
import riskyken.armourers_workshop.common.lib.LibModInfo;

public abstract class AbstractModBlockContainer extends BlockContainer implements ISortOrder {

    private int sortPriority = 100;
    
    public AbstractModBlockContainer(String name) {
        super(Material.IRON);
        setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        setHardness(3.0F);
        setSoundType(SoundType.METAL);
        setUnlocalizedName(name);
        ModBlocks.BLOCKS.add(this);
    }
    
    public AbstractModBlockContainer(String name, Material material, SoundType soundType, boolean addCreativeTab) {
        super(material);
        if (addCreativeTab) {
            setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        }
        setHardness(3.0F);
        setSoundType(soundType);
        setUnlocalizedName(name);
        ModBlocks.BLOCKS.add(this);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    protected static boolean getBitBool(int value, int index) {
        return getBit(value, index) == 1;
    }
    
    protected static int getBit(int value, int index) {
        return (value >> index) & 1;
    }

    protected static int setBit(int value, int index, boolean on) {
        if (on) {
            return value | (1 << index);
        } else {
            return value & ~(1 << index);
        }
    }
    
    @Override
    public Block setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(LibModInfo.ID, "tile." + name));
        return this;
    }

    public AbstractModBlockContainer setSortPriority(int sortPriority) {
        this.sortPriority = sortPriority;
        return this;
    }
    
    @Override
    public int getSortPriority() {
        return sortPriority;
    }
}
