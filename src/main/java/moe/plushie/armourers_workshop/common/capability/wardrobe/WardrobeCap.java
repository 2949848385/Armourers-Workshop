package moe.plushie.armourers_workshop.common.capability.wardrobe;

import java.util.ArrayList;
import java.util.HashMap;

import moe.plushie.armourers_workshop.api.common.skin.data.ISkinDye;
import moe.plushie.armourers_workshop.api.common.skin.entity.ISkinnableEntity;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.config.ConfigHandler;
import moe.plushie.armourers_workshop.common.network.PacketHandler;
import moe.plushie.armourers_workshop.common.network.messages.server.MessageServerSyncWardrobeCap;
import moe.plushie.armourers_workshop.common.skin.data.SkinDye;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class WardrobeCap implements IWardrobeCap {

    @CapabilityInject(IWardrobeCap.class)
    public static final Capability<IWardrobeCap> WARDROBE_CAP = null;

    protected final Entity entity;

    private final ISkinnableEntity skinnableEntity;

    public final ExtraColours extraColours;
    
    public final SkinDye dye;

    /** Number of slots the player has unlocked in the wardrobe */
    public HashMap<String, Integer> slotsUnlocked;

    public WardrobeCap(Entity entity, ISkinnableEntity skinnableEntity) {
        this.entity = entity;
        this.skinnableEntity = skinnableEntity;
        extraColours = new ExtraColours();
        dye = new SkinDye();
        slotsUnlocked = new HashMap<String, Integer>();
        ArrayList<ISkinType> validSkinTypes = new ArrayList<ISkinType>();
        skinnableEntity.getValidSkinTypes(validSkinTypes);
        for (int i = 0; i < validSkinTypes.size(); i++) {
            ISkinType skinType = validSkinTypes.get(i);
            slotsUnlocked.put(skinType.getRegistryName(), getUnlockedSlotsForSkinType(skinType));
        }
    }

    @Override
    public ExtraColours getExtraColours() {
        return extraColours;
    }
    
    @Override
    public ISkinDye getDye() {
        return dye;
    }

    private byte[] intToByte(int value) {
        return new byte[] { (byte) (value >>> 16 & 0xFF), (byte) (value >>> 16 & 0xFF), (byte) (value & 0xFF) };
    }

    private int byteToInt(byte[] value) {
        return 0;
    }


    public int getUnlockedSlotsForSkinType(ISkinType skinType) {
        // return skinnableEntity.getSlotsForSkinType(skinType);
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
    
    protected IMessage getUpdateMessage() {
        NBTTagCompound compound = (NBTTagCompound)WARDROBE_CAP.getStorage().writeNBT(WARDROBE_CAP, this, null);
        return new MessageServerSyncWardrobeCap(entity.getEntityId(), compound);
    }

    @Override
    public void syncToPlayerDelayed(EntityPlayerMP entityPlayer, int delay) {
        PacketHandler.sendToDelayed(getUpdateMessage(), entityPlayer, delay);
    }

    @Override
    public void syncToPlayer(EntityPlayerMP entityPlayer) {
        syncToPlayerDelayed(entityPlayer, 0);
    }

    @Override
    public void syncToAllAround() {
        PacketHandler.networkWrapper.sendToAllTracking(getUpdateMessage(), entity);
    }

    @Override
    public void sendUpdateToServer() {
    }

    public static IWardrobeCap get(Entity entity) {
        return entity.getCapability(WARDROBE_CAP, null);
    }
}
