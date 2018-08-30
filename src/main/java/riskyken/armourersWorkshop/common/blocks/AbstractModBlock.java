package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.creativetab.ISortOrder;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

public abstract class AbstractModBlock extends Block implements ISortOrder {
    
    private int sortPriority = 100;
    
    public AbstractModBlock(String name) {
        super(Material.IRON);
        setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        setHardness(3.0F);
        setSoundType(SoundType.METAL);
    }
    
    public AbstractModBlock(String name, Material material, SoundType soundType, boolean addCreativeTab) {
        super(material);
        if (addCreativeTab) {
            setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        }
        setHardness(3.0F);
        setSoundType(soundType);
    }

    @Override
    public String getUnlocalizedName() {
        return getModdedUnlocalizedName(super.getUnlocalizedName());
    }

    protected String getModdedUnlocalizedName(String unlocalizedName) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
    }
    
    public AbstractModBlock setSortPriority(int sortPriority) {
        this.sortPriority = sortPriority;
        return this;
    }
    
    @Override
    public int getSortPriority() {
        return sortPriority;
    }
}
