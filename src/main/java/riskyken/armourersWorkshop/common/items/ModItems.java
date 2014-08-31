package riskyken.armourersWorkshop.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class ModItems {
    
    public static Item customHeadArmour;
    public static Item customChestArmour;
    public static Item customLegsArmour;
    public static Item customFeetArmour;
    public static Item paintbrush;
    public static Item paintRoller;
    public static Item colourPicker;
    public static Item burnTool;
    public static Item dodgeTool;
    public static Item colourNoiseTool;
    public static Item shadeNoiseTool;
    
    public static void init() {
        customHeadArmour = new ItemCustomArmour(ArmorMaterial.DIAMOND, 0);
        customChestArmour = new ItemCustomArmour(ArmorMaterial.DIAMOND, 1);
        customLegsArmour = new ItemCustomArmour(ArmorMaterial.DIAMOND, 2);
        customFeetArmour = new ItemCustomArmour(ArmorMaterial.DIAMOND, 3);
        paintbrush = new ItemPaintbrush();
        paintRoller = new ItemPaintRoller();
        colourPicker = new ItemColourPicker();
        burnTool = new ItemBurnTool();
        dodgeTool = new ItemDodgeTool();
        colourNoiseTool = new ItemColourNoiseTool();
        shadeNoiseTool = new ItemShadeNoiseTool();
    }
}
