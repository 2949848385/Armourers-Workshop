package riskyken.armourersWorkshop.common.custom.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import riskyken.armourersWorkshop.api.common.customEquipment.armour.EnumArmourType;
import riskyken.armourersWorkshop.common.network.PacketHandler;
import riskyken.armourersWorkshop.common.network.messages.MessageServerAddEquipmentInfo;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class ExtendedPropsEntityEquipmentData implements IExtendedEntityProperties {
    
    public static final String TAG_EXT_PROP_NAME = "entityCustomEquipmentData";
    
    private EntityEquipmentData equipmentData = new EntityEquipmentData();
    private Entity entity;
    
    public ExtendedPropsEntityEquipmentData(Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        equipmentData.saveNBTData(compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        equipmentData.loadNBTData(compound);
    }

    @Override
    public void init(Entity entity, World world) {
    }
    
    public void addCustomEquipment(EnumArmourType type, int equipmentId) {
        equipmentData.addEquipment(type, equipmentId);
        TargetPoint p = new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 512);
        PacketHandler.networkWrapper.sendToAllAround(new MessageServerAddEquipmentInfo(entity.getPersistentID(), equipmentData), p);
    }

    public void removeCustomEquipment(EnumArmourType type) {
        equipmentData.removeEquipment(type);
        TargetPoint p = new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 512);
        PacketHandler.networkWrapper.sendToAllAround(new MessageServerAddEquipmentInfo(entity.getPersistentID(), equipmentData), p);
    }
    
    public boolean hasCustomEquipment() {
        return equipmentData.hasCustomEquipment();
    }
    
    public void sendCustomEquipmentDataToPlayer(EntityPlayerMP targetPlayer) {
        PacketHandler.networkWrapper.sendTo(new MessageServerAddEquipmentInfo(entity.getPersistentID(), equipmentData), targetPlayer);
    }
    
    public EntityEquipmentData getEquipmentData() {
        return equipmentData;
    }
    
    public static final void register(Entity entity) {
        entity.registerExtendedProperties(ExtendedPropsEntityEquipmentData.TAG_EXT_PROP_NAME, new ExtendedPropsEntityEquipmentData(entity));
    }
    
    public static final ExtendedPropsEntityEquipmentData get(Entity entity) {
        return (ExtendedPropsEntityEquipmentData) entity.getExtendedProperties(TAG_EXT_PROP_NAME);
    }
}
