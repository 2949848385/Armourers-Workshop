package riskyken.armourersWorkshop.common.items;

import net.minecraft.item.Item;

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
    public static Item guideBook;
    
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
        guideBook = new ItemGuideBook();
    }
}
