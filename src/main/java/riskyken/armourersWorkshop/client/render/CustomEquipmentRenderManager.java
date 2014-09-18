package riskyken.armourersWorkshop.client.render;

import java.util.BitSet;
import java.util.HashMap;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import riskyken.armourersWorkshop.client.model.ModelCustomArmourChest;
import riskyken.armourersWorkshop.client.model.ModelCustomArmourFeet;
import riskyken.armourersWorkshop.client.model.ModelCustomArmourHead;
import riskyken.armourersWorkshop.client.model.ModelCustomArmourLegs;
import riskyken.armourersWorkshop.client.model.ModelCustomArmourSkirt;
import riskyken.armourersWorkshop.common.custom.equipment.armour.ArmourType;
import riskyken.armourersWorkshop.common.custom.equipment.data.CustomArmourItemData;
import riskyken.armourersWorkshop.utils.ModLogger;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomEquipmentRenderManager {
    
    public HashMap<String, CustomArmourItemData> customArmor = new HashMap<String, CustomArmourItemData>();
    public HashMap<String, PlayerSkinInfo> skinMap = new HashMap<String, PlayerSkinInfo>();
    
    public ModelCustomArmourChest customChest = new ModelCustomArmourChest();
    public ModelCustomArmourHead customHead = new ModelCustomArmourHead();
    public ModelCustomArmourLegs customLegs = new ModelCustomArmourLegs();
    public ModelCustomArmourSkirt customSkirt = new ModelCustomArmourSkirt();
    public ModelCustomArmourFeet customFeet = new ModelCustomArmourFeet();
    
    public CustomEquipmentRenderManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public CustomArmourItemData getPlayerCustomArmour(Entity entity, ArmourType type) {
        if (!(entity instanceof AbstractClientPlayer)) { return null; }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        String key = player.getDisplayName() + ":" + type.name();
        if (!customArmor.containsKey(key)) {
            return null;
        }

        CustomArmourItemData armorData = customArmor.get(key);
        if (armorData.getType() != type) { return null; }
        return armorData;
    }
    
    public void addCustomArmour(String playerName, CustomArmourItemData armourData) {
        String key = playerName + ":" + armourData.getType().name();
        if (customArmor.containsKey(key)) {
            customArmor.remove(key);
        }
        armourData.removeHiddenBlocks();
        customArmor.put(key, armourData);
    }
    
    public void setPlayersSkinData(String playerName, boolean isNaked, int skinColour, int pantsColour, BitSet armourOverride, boolean headOverlay) {
        if (!skinMap.containsKey(playerName)) {
            skinMap.put(playerName, new PlayerSkinInfo(isNaked, skinColour, pantsColour, armourOverride, headOverlay));
        } else {
            skinMap.get(playerName).setSkinInfo(isNaked, skinColour, pantsColour, armourOverride, headOverlay);
        }
    }
    
    public PlayerSkinInfo getPlayersNakedData(String playerName) {
        if (!skinMap.containsKey(playerName)) {
            return null;
        }
        return skinMap.get(playerName);
    }
    
    public void removeCustomArmour(String playerName, ArmourType type) {
        String key = playerName + ":" + type.name();
        if (customArmor.containsKey(key)) {
            customArmor.remove(key);
        }
    }

    public void removeAllCustomArmourData(String playerName) {
        removeCustomArmour(playerName, ArmourType.HEAD);
        removeCustomArmour(playerName, ArmourType.CHEST);
        removeCustomArmour(playerName, ArmourType.LEGS);
        removeCustomArmour(playerName, ArmourType.SKIRT);
        removeCustomArmour(playerName, ArmourType.FEET);
    }

    private boolean playerHasCustomArmourType(String playerName, ArmourType armourType) {
        String key = playerName + ":" + armourType.name();
        return customArmor.containsKey(key);
    }
    
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        if (skinMap.containsKey(player.getDisplayName())) {
            PlayerSkinInfo skinInfo = skinMap.get(player.getDisplayName());
            skinInfo.preRender((AbstractClientPlayer) player, event.renderer);
        }
        
        if (playerHasCustomArmourType(player.getDisplayName(), ArmourType.SKIRT)) {
            if (player.limbSwingAmount > 0.25F) {
                player.limbSwingAmount = 0.25F;
            }
        }
    }
    
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Post event) {
    	EntityPlayer player = event.entityPlayer;
    	if (skinMap.containsKey(player.getDisplayName())) {
    		PlayerSkinInfo skinInfo = skinMap.get(player.getDisplayName());
    		skinInfo.postRender((AbstractClientPlayer) player, event.renderer);
    	}
    }
    
    public int getCacheSize() {
        return customArmor.size();
    }
    
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.SetArmorModel event) {
        EntityPlayer player = event.entityPlayer;
        RenderPlayer render = event.renderer;
        PlayerSkinInfo skinInfo = null;
        int result = -1;
        int slot = -event.slot + 3;
        
    	if (skinMap.containsKey(player.getDisplayName())) {
    		skinInfo = skinMap.get(player.getDisplayName());
    		BitSet armourOverride = skinInfo.getArmourOverride();
    		if (armourOverride.get(slot)) {
    			result = -2;
    		}
    	}
        
        if (slot == ArmourType.HEAD.getSlotId()) {
            CustomArmourItemData data = getPlayerCustomArmour(player, ArmourType.HEAD);
            if (data != null) {
                customHead.render(player, render, data);
            }
            
        }
        if (slot == ArmourType.CHEST.getSlotId()) {
            CustomArmourItemData data = getPlayerCustomArmour(player, ArmourType.CHEST);
            if (data != null) {
                customChest.render(player, render, data);
            }
        }
        if (slot == ArmourType.LEGS.getSlotId()) {
            CustomArmourItemData data = getPlayerCustomArmour(player, ArmourType.LEGS);
            if (data != null) {
                customLegs.render(player, render, data);
                event.result = result;
            }
        }
        if (slot == ArmourType.SKIRT.getSlotId()) {
            CustomArmourItemData data = getPlayerCustomArmour(player, ArmourType.SKIRT);
            if (data != null) {
                customSkirt.render(player, render, data);
            }
        }
        if (slot == ArmourType.FEET.getSlotId()) {
            CustomArmourItemData data = getPlayerCustomArmour(player, ArmourType.FEET);
            if (data != null) {
                customFeet.render(player, render, data);
            }
        }
        event.result = result;
    }
}
