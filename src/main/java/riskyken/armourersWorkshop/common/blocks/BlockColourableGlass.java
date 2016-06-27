package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColourableGlass extends BlockColourable {

    public BlockColourableGlass(String name, boolean glowing) {
        super(name, glowing);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getBlockState(pos).getBlock() == this) {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
