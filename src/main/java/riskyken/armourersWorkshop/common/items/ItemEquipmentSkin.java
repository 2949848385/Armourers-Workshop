package riskyken.armourersWorkshop.common.items;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import org.lwjgl.input.Keyboard;

import riskyken.armourersWorkshop.api.common.equipment.skin.IEquipmentSkinType;
import riskyken.armourersWorkshop.client.equipment.ClientEquipmentModelCache;
import riskyken.armourersWorkshop.client.lib.LibItemResources;
import riskyken.armourersWorkshop.client.settings.Keybindings;
import riskyken.armourersWorkshop.common.equipment.cubes.Cube;
import riskyken.armourersWorkshop.common.equipment.cubes.CubeGlass;
import riskyken.armourersWorkshop.common.equipment.cubes.CubeGlassGlowing;
import riskyken.armourersWorkshop.common.equipment.cubes.CubeGlowing;
import riskyken.armourersWorkshop.common.equipment.data.EquipmentSkinTypeData;
import riskyken.armourersWorkshop.common.equipment.skin.SkinTypeRegistry;
import riskyken.armourersWorkshop.common.handler.EquipmentDataHandler;
import riskyken.armourersWorkshop.common.lib.LibItemNames;
import riskyken.armourersWorkshop.utils.EquipmentNBTHelper;
import riskyken.armourersWorkshop.utils.EquipmentNBTHelper.SkinNBTData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEquipmentSkin extends AbstractModItem {

    public ItemEquipmentSkin() {
        super(LibItemNames.EQUIPMENT_SKIN, false);
    }
    
    public IEquipmentSkinType getSkinType(ItemStack stack) {
        return EquipmentDataHandler.INSTANCE.getSkinTypeFromStack(stack);
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        String cGreen = EnumChatFormatting.GREEN.toString();
        String cGray = EnumChatFormatting.GRAY.toString();
        String cRed = EnumChatFormatting.RED.toString();
        String cGold = EnumChatFormatting.GOLD.toString();
        String cYellow = EnumChatFormatting.YELLOW.toString();
        
        if (EquipmentNBTHelper.stackHasSkinData(stack)) {
            SkinNBTData skinData = EquipmentNBTHelper.getSkinNBTDataFromStack(stack);
            if (ClientEquipmentModelCache.INSTANCE.isEquipmentInCache(skinData.skinId)) {
                EquipmentSkinTypeData data = ClientEquipmentModelCache.INSTANCE.getEquipmentItemData(skinData.skinId);
                if (!data.getCustomName().trim().isEmpty()) {
                    list.add(cGold + "Name: " + cGray + data.getCustomName());
                }
                if (!data.getAuthorName().trim().isEmpty()) {
                    list.add(cGold + "Author: " + cGray + data.getAuthorName());
                }
                
                if (skinData.skinType != null) {
                    list.add(cGold + "Skin Type: " + cGray + SkinTypeRegistry.INSTANCE.getLocalizedSkinTypeName(skinData.skinType));
                }
                
                if (GuiScreen.isShiftKeyDown()) {
                    list.add(cYellow + "Equipment Id: " + cGray + skinData.skinId);
                    list.add(cYellow + "Total Cubes: " + cGray + data.getTotalCubes());
                    list.add(cYellow + "Cubes: " + cGray + data.getTotalOfCubeType(Cube.class));
                    list.add(cYellow + "Cubes Glowing: " + cGray + data.getTotalOfCubeType(CubeGlowing.class));
                    list.add(cYellow + "Cubes Glass: " + cGray + data.getTotalOfCubeType(CubeGlass.class));
                    list.add(cYellow + "Cubes Glass Glowing: " + cGray + data.getTotalOfCubeType(CubeGlassGlowing.class));
                    
                } else {
                    list.add("Hold " + cGreen + "shift" + cGray + " for debug info.");
                }
            } else {
                list.add("Downloading skin...");
            }
            
            String keyName = Keyboard.getKeyName(Keybindings.openCustomArmourGui.getKeyCode());
            keyName = cGreen + keyName + cGray;
            list.add("Press the " + keyName + " key to open the " + cGreen + "Equipment Wardrobe");
        } else {
            if (EquipmentNBTHelper.stackHasLegacySkinData(stack)) {
                list.add(cRed + "Old skin type. Place in crafting grid to restore.");
            } else {
                list.add(cRed + "ERROR: Invalid equipment skin.");
                list.add(cRed + "Please delete.");
            }
        }

        super.addInformation(stack, player, list, p_77624_4_);
    }
    
    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }
    
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    private IIcon loadingIcon;
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(LibItemResources.TEMPLATE_BLANK);
        this.loadingIcon = register.registerIcon(LibItemResources.TEMPLATE_LOADING);
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1) {
            return this.loadingIcon;
        }
        
        if (EquipmentNBTHelper.stackHasSkinData(stack)) {
            SkinNBTData skinData = EquipmentNBTHelper.getSkinNBTDataFromStack(stack);
            if (skinData.skinType != null) {
                if (skinData.skinType.getIcon() != null) {
                    return skinData.skinType.getIcon();
                }
            }
        }
        
        return this.itemIcon;
    }
}
