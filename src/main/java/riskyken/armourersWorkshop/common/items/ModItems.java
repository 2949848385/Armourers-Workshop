package riskyken.armourersWorkshop.common.items;

import net.minecraft.item.Item;
import riskyken.armourersWorkshop.common.lib.LibItemNames;

public class ModItems {
    public static Item equipmentSkinTemplate;
    public static Item equipmentSkin;
    public static Item paintbrush;
    public static Item paintRoller;
    public static Item colourPicker;
    public static Item burnTool;
    public static Item dodgeTool;
    public static Item colourNoiseTool;
    public static Item shadeNoiseTool;
    public static Item mannequinTool;
    public static Item guideBook;
    public static Item[] armourContainer;
    
    public static void init() {
        equipmentSkinTemplate = new ItemEquipmentSkinTemplate();
        equipmentSkin = new ItemEquipmentSkin();
        paintbrush = new ItemPaintbrush();
        paintRoller = new ItemPaintRoller();
        colourPicker = new ItemColourPicker();
        burnTool = new ItemBurnTool();
        dodgeTool = new ItemDodgeTool();
        colourNoiseTool = new ItemColourNoiseTool();
        shadeNoiseTool = new ItemShadeNoiseTool();
        mannequinTool = new ItemMannequinTool();
        guideBook = new ItemGuideBook();
        armourContainer = new Item[4];
        armourContainer[0] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_HEAD, 0);
        armourContainer[1] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_CHEST, 1);
        armourContainer[2] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_LEGS, 2);
        armourContainer[3] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_FEET, 3);
    }
}
