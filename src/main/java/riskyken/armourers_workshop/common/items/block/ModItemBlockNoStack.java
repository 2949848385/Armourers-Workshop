package riskyken.armourers_workshop.common.items.block;

import net.minecraft.block.Block;

public class ModItemBlockNoStack extends ModItemBlock {

    public ModItemBlockNoStack(Block block) {
        super(block);
        setMaxStackSize(1);
    }
}
