package riskyken.armourersWorkshop.common.tileentities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import riskyken.armourersWorkshop.common.items.ItemArmourTemplate;
import riskyken.armourersWorkshop.common.items.ItemCustomArmour;
import riskyken.armourersWorkshop.common.items.ModItems;
import riskyken.armourersWorkshop.common.lib.LibBlockNames;

public class TileEntityArmourCrafter extends AbstractTileEntityInventory {

    private static final String TAG_ARMOUR_DATA = "armourData";
    
    public TileEntityArmourCrafter() {
        this.items = new ItemStack[3];
    }
    
    @Override
    public String getInventoryName() {
        return LibBlockNames.ARMOUR_CRAFTER;
    }

    public void createArmour(EntityPlayerMP player) {
        ItemStack stackTemplate = getStackInSlot(0);
        ItemStack stackArmour = getStackInSlot(1);
        
        if (stackTemplate == null | stackArmour == null) { return; }
        if (!stackTemplate.hasTagCompound()) { return; }

        if (!(stackTemplate.getItem() instanceof ItemArmourTemplate)) { return; }
        if (!(stackArmour.getItem() instanceof ItemArmor)) { return; }
        
        ItemArmor itemArmour = (ItemArmor)stackArmour.getItem();
        ItemArmourTemplate itemTemplate = (ItemArmourTemplate)stackTemplate.getItem();
        
        if (itemArmour.armorType != itemTemplate.getArmourTypeNomal(stackTemplate)) { return; }
        
        ItemCustomArmour targetArmour;
        
        switch (itemTemplate.getArmourType(stackTemplate)) {
        case 0:
            targetArmour = ModItems.customHeadArmour[itemArmour.getArmorMaterial().ordinal()];
            break;
        case 1:
            targetArmour = ModItems.customChestArmour[itemArmour.getArmorMaterial().ordinal()];
            break;
        case 2:
            targetArmour = ModItems.customLegsArmour[itemArmour.getArmorMaterial().ordinal()];
            break;
        case 3:
            targetArmour = ModItems.customSkirtArmour[itemArmour.getArmorMaterial().ordinal()];
            break;
        case 4:
            targetArmour = ModItems.customFeetArmour[itemArmour.getArmorMaterial().ordinal()];
            break;
        default:
            return;
        }
        
        ItemStack stack = new ItemStack(targetArmour, 1, stackArmour.getItemDamage());
        
        stack.setTagCompound(stackTemplate.getTagCompound());
        
        setInventorySlotContents(1, null);
        setInventorySlotContents(2, stack);
        
    }
}
