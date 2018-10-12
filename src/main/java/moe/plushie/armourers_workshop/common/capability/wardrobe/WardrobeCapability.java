package moe.plushie.armourers_workshop.common.capability.wardrobe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import moe.plushie.armourers_workshop.api.common.skin.entity.ISkinnableEntity;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.config.ConfigHandler;
import moe.plushie.armourers_workshop.common.network.PacketHandler;
import moe.plushie.armourers_workshop.common.network.messages.client.MessageClientUpdateWardrobeCap;
import moe.plushie.armourers_workshop.common.network.messages.server.MessageServerSyncWardrobeCap;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class WardrobeCapability implements IWardrobeCapability {

    @CapabilityInject(IWardrobeCapability.class)
    public static final Capability<IWardrobeCapability> WARDROBE_CAP = null;

    public static final Color COLOUR_SKIN_DEFAULT = Color.decode("#F9DFD2");

    public static final Color COLOUR_HAIR_DEFAULT = Color.decode("#804020");

    private final EntityPlayer entityPlayer;

    private final ISkinnableEntity skinnableEntity;
    
    public final int[] extraColours;

    /** Bit set of what armour is hidden on the player. */
    public BitSet armourOverride;

    /** Number of slots the player has unlocked in the wardrobe */
    public HashMap<String, Integer> slotsUnlocked;

    public WardrobeCapability(EntityPlayer entityPlayer, ISkinnableEntity skinnableEntity) {
        this.entityPlayer = entityPlayer;
        this.skinnableEntity = skinnableEntity;
        
        extraColours = new int[ExtraColourType.values().length];
        
        setExtraColour(ExtraColourType.SKIN, COLOUR_SKIN_DEFAULT.getRGB());
        setExtraColour(ExtraColourType.HAIR, COLOUR_HAIR_DEFAULT.getRGB());
        setExtraColour(ExtraColourType.EYE, 0xFFFFFF);
        setExtraColour(ExtraColourType.ACC, 0xFFFFFF);
        
        armourOverride = new BitSet(4);
        slotsUnlocked = new HashMap<String, Integer>();
        ArrayList<ISkinType> validSkinTypes = new ArrayList<ISkinType>();
        skinnableEntity.getValidSkinTypes(validSkinTypes);
        for (int i = 0; i < validSkinTypes.size(); i++) {
            ISkinType skinType = validSkinTypes.get(i);
            slotsUnlocked.put(skinType.getRegistryName(), getUnlockedSlotsForSkinType(skinType));
        }
    }
    
    @Override
    public int getExtraColour(ExtraColourType type) {
        return extraColours[type.ordinal()];
    }
    
    @Override
    public byte[] getExtraColourByte(ExtraColourType type) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setExtraColour(ExtraColourType type, int colour) {
        extraColours[type.ordinal()] = colour;
    }
    
    @Override
    public void setExtraColourByte(ExtraColourType type, byte[] colour) {
        //extraColours[type.ordinal()] = colour
    }
    
    @Override
    public byte[] getAllExtraColours() {
        return new byte[] {};
        
        /*ec[0] = (byte) (skin >>> 16 & 0xFF);
        ec[1] = (byte) (skin >>> 8 & 0xFF);
        ec[2] = (byte) (skin & 0xFF);*/
    }
    
    private byte[] intToByte(int value) {
        return new byte[] {(byte) (value >>> 16 & 0xFF), (byte) (value >>> 16 & 0xFF), (byte) (value & 0xFF)};
    }
    
    private int byteToInt(byte[] value) {
        return 0;
    }
    
    @Override
    public BitSet getArmourOverride() {
        return armourOverride;
    }
    
    @Override
    public void setArmourOverride(BitSet armourOverride) {
        this.armourOverride = armourOverride;
    }

    public int getUnlockedSlotsForSkinType(ISkinType skinType) {
        //return skinnableEntity.getSlotsForSkinType(skinType);
        if (skinType == SkinTypeRegistry.skinBow) {
            return 1;
        }
        if (skinType == SkinTypeRegistry.skinSword) {
            return 5;
        }
        if (slotsUnlocked.containsKey(skinType.getRegistryName())) {
            return slotsUnlocked.get(skinType.getRegistryName());
        } else {
            return ConfigHandler.startingWardrobeSlots;
        }
    }

    public void setUnlockedSlotsForSkinType(ISkinType skinType, int value) {
        slotsUnlocked.put(skinType.getRegistryName(), value);
    }

    @Override
    public void syncToPlayerDelayed(EntityPlayerMP entityPlayer, int delay) {
        IStorage<IWardrobeCapability> storage = WARDROBE_CAP.getStorage();
        NBTTagCompound compound = (NBTTagCompound) storage.writeNBT(WARDROBE_CAP, this, null);
        MessageServerSyncWardrobeCap message = new MessageServerSyncWardrobeCap(entityPlayer.getEntityId(), compound);
        PacketHandler.sendToDelayed(message, entityPlayer, delay);
    }

    @Override
    public void syncToPlayer(EntityPlayerMP entityPlayer) {
        syncToPlayerDelayed(entityPlayer, 0);
    }

    @Override
    public void syncToAllAround() {
        NBTTagCompound compound = (NBTTagCompound) WARDROBE_CAP.getStorage().writeNBT(WARDROBE_CAP, this, null);
        MessageServerSyncWardrobeCap message = new MessageServerSyncWardrobeCap(entityPlayer.getEntityId(), compound);
        PacketHandler.networkWrapper.sendToAllTracking(message, entityPlayer);
    }
    
    @Override
    public void sendUpdateToServer() {
        NBTTagCompound compound = (NBTTagCompound) WARDROBE_CAP.getStorage().writeNBT(WARDROBE_CAP, this, null);
        MessageClientUpdateWardrobeCap message = new MessageClientUpdateWardrobeCap(compound);
        PacketHandler.networkWrapper.sendToServer(message);
    }

    public static IWardrobeCapability get(EntityPlayer entity) {
        return entity.getCapability(WARDROBE_CAP, null);
    }
}
