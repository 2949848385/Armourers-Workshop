package riskyken.armourers_workshop.common.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import riskyken.armourers_workshop.common.data.BipedRotations;
import riskyken.armourers_workshop.common.lib.LibItemNames;
import riskyken.armourers_workshop.utils.NBTHelper;

public class ItemMannequinTool extends AbstractModItem {
    
    private static final String TAG_ROTATION_DATA = "rotationData";

    public ItemMannequinTool() {
        super(LibItemNames.MANNEQUIN_TOOL);
        setSortPriority(10);
    }
    /*
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
            int side, float hitX, float hitY, float hitZ) {
        
        Block block = world.getBlock(x, y, z);
        if (block != null && (block == ModBlocks.mannequin | block == ModBlocks.doll)) {
            TileEntity te;
            int meta = world.getBlockMetadata(x, y, z);
            if (meta == 0) {
                te = world.getTileEntity(x, y, z);
            } else {
                te = world.getTileEntity(x, y - 1, z);
            }
            if (te != null && te instanceof TileEntityMannequin) {
                TileEntityMannequin teMan = (TileEntityMannequin) te;
                if (player.isSneaking()) {
                    setRotationDataOnStack(stack, teMan.getBipedRotations());
                } else {
                    BipedRotations bipedRotations = getRotationDataFromStack(stack);
                    if (bipedRotations != null) {
                        teMan.setBipedRotations(bipedRotations);
                    }
                }
                return true;
            }
        }
        return false;
    }*/
    
    private BipedRotations getRotationDataFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return null;
        }
        NBTTagCompound compound = stack.getTagCompound();
        if (!compound.hasKey(TAG_ROTATION_DATA)) {
            return null;
        }
        NBTTagCompound rotationCompound = compound.getCompoundTag(TAG_ROTATION_DATA);
        BipedRotations bipedRotations = new BipedRotations();
        bipedRotations.loadNBTData(rotationCompound);
        return bipedRotations;
    }
    
    private void setRotationDataOnStack(ItemStack stack, BipedRotations bipedRotations) {
        NBTTagCompound compound = NBTHelper.getNBTForStack(stack);
        NBTTagCompound rotationCompound = new NBTTagCompound();
        bipedRotations.saveNBTData(rotationCompound);
        compound.setTag(TAG_ROTATION_DATA, rotationCompound);
        stack.setTagCompound(compound);
    }
    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        super.addInformation(stack, player, list, p_77624_4_);
        if (stack.hasTagCompound()) {
            String settingsSaved = TranslateUtils.translate("item.armourersworkshop:rollover.settingsSaved");
            list.add(settingsSaved);
        } else {
            String noSettingsSaved = TranslateUtils.translate("item.armourersworkshop:rollover.noSettingsSaved");
            list.add(noSettingsSaved);
        }
    }*/
}
