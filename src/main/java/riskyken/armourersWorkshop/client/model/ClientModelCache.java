package riskyken.armourersWorkshop.client.model;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.Level;

import riskyken.armourersWorkshop.common.network.PacketHandler;
import riskyken.armourersWorkshop.common.network.messages.MessageClientRequestEquipmentDataData;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.utils.ModLogger;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientModelCache {
    
    public static ClientModelCache INSTANCE;
    
    private final HashMap<Integer, Skin> equipmentDataMap;
    private final HashSet<Integer> requestedEquipmentIds;
    
    public static void init() {
        INSTANCE = new ClientModelCache();
    }
    
    public ClientModelCache() {
        equipmentDataMap = new HashMap<Integer, Skin>();
        requestedEquipmentIds = new HashSet<Integer>();
        FMLCommonHandler.instance().bus().register(this);
    }
    
    public void requestEquipmentDataFromServer(int equipmentId) {
        synchronized (requestedEquipmentIds) {
            if (!requestedEquipmentIds.contains(equipmentId)) {
                PacketHandler.networkWrapper.sendToServer(new MessageClientRequestEquipmentDataData(equipmentId));
                requestedEquipmentIds.add(equipmentId);
            }
        }
    }
    
    public boolean isEquipmentInCache(int equipmentId) {
        synchronized (equipmentDataMap) {
            return equipmentDataMap.containsKey(equipmentId); 
        }
    }
    
    public void receivedModelFromBakery(Skin equipmentData) {
        int equipmentId = equipmentData.lightHash();
        
        synchronized (equipmentDataMap) {
            if (equipmentDataMap.containsKey(equipmentId)) {
                Skin oldSkin = equipmentDataMap.get(equipmentId);
                equipmentDataMap.remove(equipmentId);
                oldSkin.cleanUpDisplayLists();
            }
        }
        
        synchronized (requestedEquipmentIds) {
            if (requestedEquipmentIds.contains(equipmentId)) {
                synchronized (equipmentDataMap) {
                    equipmentDataMap.put(equipmentId, equipmentData);
                }
                requestedEquipmentIds.remove(equipmentId);
            } else {
                if (requestedEquipmentIds.contains(equipmentData.requestId)) {
                    //Out of date skin
                    synchronized (equipmentDataMap) {
                        equipmentDataMap.put(equipmentData.requestId, equipmentData);
                    }
                    requestedEquipmentIds.remove(equipmentData.requestId);
                } else {
                    ModLogger.log(Level.WARN, "Got an unknown equipment id: " + equipmentId);
                }
            }
        }
    }
    
    public int getCacheSize() {
        synchronized (equipmentDataMap) {
            return equipmentDataMap.size();
        }
    }
    
    public void clearCache() {
        synchronized (equipmentDataMap) {
            equipmentDataMap.clear();
        }
    }
    
    public Skin getEquipmentItemData(int equipmentId) {
        synchronized (equipmentDataMap) {
            if (equipmentDataMap.containsKey(equipmentId)) {
                return equipmentDataMap.get(equipmentId);
            }
        }
        requestEquipmentDataFromServer(equipmentId);
        return null;
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side == Side.CLIENT & event.type == Type.CLIENT & event.phase == Phase.END) {
            tick();
        }
    }
    
    public void tick() {
        synchronized (equipmentDataMap) {
            for (int i = 0; i < equipmentDataMap.size(); i++) {
                int key = (Integer) equipmentDataMap.keySet().toArray()[i];
                equipmentDataMap.get(key).tick();
            }
            
            for (int i = 0; i < equipmentDataMap.size(); i++) {
                int key = (Integer) equipmentDataMap.keySet().toArray()[i];
                Skin customArmourItemData = equipmentDataMap.get(key);
                if (customArmourItemData.needsCleanup()) {
                    equipmentDataMap.remove(key);
                    customArmourItemData.cleanUpDisplayLists();
                    break;
                }
            }
        }
    }
}
