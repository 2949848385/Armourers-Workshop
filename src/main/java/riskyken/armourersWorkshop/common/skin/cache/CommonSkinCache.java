package riskyken.armourersWorkshop.common.skin.cache;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayerMP;
import riskyken.armourersWorkshop.api.common.library.ILibraryFile;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinIdentifier;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinPointer;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.common.config.ConfigHandler;
import riskyken.armourersWorkshop.common.data.ExpiringHashMap.IExpiringMapCallback;
import riskyken.armourersWorkshop.common.library.LibraryFile;
import riskyken.armourersWorkshop.common.network.PacketHandler;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerSendSkinData;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinIdentifier;
import riskyken.armourersWorkshop.utils.ModLogger;
import riskyken.armourersWorkshop.utils.SkinIOUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Holds a cache of equipment data on the server that will be sent to clients if
 * they request it.
 * 
 * @author RiskyKen
 *
 */
public final class CommonSkinCache implements Runnable, IExpiringMapCallback<Skin> {
    
    public static final CommonSkinCache INSTANCE = new CommonSkinCache();
    
    private final SkinCacheLocalDatabase cacheLocalDatabase;
    private final SkinCacheLocalFile cacheLocalFile;
    private final SkinCacheGlobal cacheGlobal;
    
    private volatile Thread serverSkinThread = null;
    
    private ArrayList<SkinRequestMessage> messageQueue = new ArrayList<SkinRequestMessage>();
    private final Object messageQueueLock = new Object();
    
    private ArrayList<SkinRequestMessage> messageWaitQueue = new ArrayList<SkinRequestMessage>();
    private final Object messageWaitQueueLock = new Object();
    
    private long lastMessageSendTick;
    private boolean madeDatabase = false;
    
    public CommonSkinCache() {
        cacheLocalDatabase = new SkinCacheLocalDatabase(this);
        cacheLocalFile = new SkinCacheLocalFile(cacheLocalDatabase);
        cacheGlobal = new SkinCacheGlobal(cacheLocalDatabase);
    }
    
    public void clearAll() {
        cacheLocalDatabase.clear();
        cacheLocalFile.clear();
        cacheGlobal.clear();
    }
    
    public void serverStarted() {
        SkinIOUtils.makeDatabaseDirectory();
        serverSkinThread = new Thread(this, "Armourer's Workshop Server Skin Thread");
        serverSkinThread.start();
    }
    
    public void serverStopped() {
        clearAll();
        serverSkinThread = null;
    }
    
    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        ModLogger.log("Starting server skin thread.");
        while (serverSkinThread == thisThread) {
            try {
                thisThread.sleep(10);
            } catch (InterruptedException e) {
            }
            cacheLocalDatabase.doSkinLoading();
            cacheLocalFile.doSkinLoading();
            cacheGlobal.doSkinLoading();
            processMessageQueue();
        }
        ModLogger.log("Stopped server skin thread.");
    }
    
    public void clientRequestEquipmentData(ISkinIdentifier skinIdentifier, EntityPlayerMP player) {
        SkinRequestMessage queueMessage = new SkinRequestMessage(skinIdentifier, player);
        synchronized (messageQueueLock) {
            messageQueue.add(queueMessage);
        }
    }
    
    private void processMessageQueue() {
        if (ConfigHandler.serverSkinSendRate > 1) {
            long curTick = System.currentTimeMillis();
            if (curTick >= lastMessageSendTick + (60000 / ConfigHandler.serverSkinSendRate)) {
                lastMessageSendTick = curTick;
                processNextMessage();
            }
        } else {
            processNextMessage();
        }
    }
    
    private void processNextMessage() {
        synchronized (messageQueueLock) {
            if (messageQueue.size() > 0) {
                processMessage(messageQueue.get(0));
                messageQueue.remove(0);
            }
        }
    }
    
    public Skin addSkinToCache(InputStream inputStream) {
        Skin skin = SkinIOUtils.loadSkinFromStream(inputStream);
        if (skin != null) {
            addEquipmentDataToCache(skin, (LibraryFile)null);
            return skin;
        }
        return null;
    }
    
    private void processMessage(SkinRequestMessage queueMessage) {
        ISkinIdentifier identifier = queueMessage.getSkinIdentifier();
        EntityPlayerMP player = queueMessage.getPlayer();
        if (identifier.hasLocalId()) {
            sendLocalDatabaseSkinToClient(queueMessage);
        } else if (identifier.hasLibraryFile()) {
            sendLocalFileSkinToClient(queueMessage);
        } else if (identifier.hasGlobalId()) {
            sendGlobalDatabaseSkinToClient(queueMessage);
        } else {
            ModLogger.log(Level.ERROR, "Player " + player.getCommandSenderName() + " requested a skin with no vaid ID:" + identifier.toString());
        }
    }
    
    private void sendLocalDatabaseSkinToClient(SkinRequestMessage requestMessage) {
        Skin skin = cacheLocalDatabase.get(requestMessage, true);
        if (skin != null) {
            sendSkinToClient(skin, requestMessage);
        } else {
            synchronized (messageWaitQueueLock) {
                messageWaitQueue.add(requestMessage);
            }
        }
    }
    
    private void sendLocalFileSkinToClient(SkinRequestMessage requestMessage) {
        Skin skin = cacheLocalFile.get(requestMessage, true);
        if (skin != null) {
            sendSkinToClient(skin, requestMessage);
        } else {
            synchronized (messageWaitQueueLock) {
                messageWaitQueue.add(requestMessage);
            }
        }
    }
    
    private void sendGlobalDatabaseSkinToClient(SkinRequestMessage requestMessage) {
        Skin skin = cacheGlobal.get(requestMessage, true);
        if (skin != null) {
            sendSkinToClient(skin, requestMessage);
        } else {
            synchronized (messageWaitQueueLock) {
                messageWaitQueue.add(requestMessage);
            }
        }
    }
    
    public void onSkinLoaded(Skin skin, SkinRequestMessage requestMessage) {
        if (requestMessage.getPlayer() == null) {
            return;
        }
        synchronized (messageWaitQueueLock) {
            for (int i = 0; i < messageWaitQueue.size(); i++) {
                if (messageWaitQueue.get(i).getSkinIdentifier() == requestMessage.getSkinIdentifier()) {
                    sendSkinToClient(skin, requestMessage);
                    messageWaitQueue.remove(i);
                }
            }
        }
    }
    
    public void onGlobalSkinLoaded(Skin skin, int globalId) {
        //ModLogger.log(String.format("Skin downloaded from global library id: %d.", globalId));
        synchronized (messageWaitQueueLock) {
            for (int i = 0; i < messageWaitQueue.size(); i++) {
                if (messageWaitQueue.get(i).getSkinIdentifier().getSkinGlobalId() == globalId) {
                    sendSkinToClient(skin, messageWaitQueue.get(i));
                    messageWaitQueue.remove(i);
                }
            }
        }
    }
    
    private void sendSkinToClient(Skin skin, SkinRequestMessage requestMessage) {
        SkinIdentifier requestIdentifier = (SkinIdentifier) requestMessage.getSkinIdentifier();
        skin.requestId = requestIdentifier;
        PacketHandler.networkWrapper.sendTo(new MessageServerSendSkinData(requestIdentifier, getFullIdentifier(skin, requestIdentifier), skin), requestMessage.getPlayer());
    }
    
    public SkinIdentifier getFullIdentifier(Skin skin, ISkinIdentifier skinIdentifier) {
        int localId = skin.lightHash();
        ISkinType skinType = skin.getSkinType();
        ILibraryFile libraryFile = null;
        int globalId = 0;
        
        
        try {
            if (cacheLocalFile.containsValue(skin.lightHash())) {
                libraryFile = cacheLocalFile.getBackward(skin.lightHash());
            }
            
            if (cacheGlobal.containsValue(skin.lightHash())) {
                globalId = cacheGlobal.getBackward(skin.lightHash());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        return new SkinIdentifier(localId, libraryFile, globalId, skinType);
    }
    
    public void addEquipmentDataToCache(Skin skin, LibraryFile libraryFile) {
        cacheLocalDatabase.add(skin);
        if (libraryFile != null) {
            cacheLocalFile.add(libraryFile, skin.lightHash());
        }
    }
    
    @Deprecated
    public void addEquipmentDataToCache(Skin skin, String fileName) {
        throw new NotImplementedException();
        // TODO Edit anything pointing to this.
        /*
        try {
            skin.lightHash();
        } catch (Exception e) {
            ModLogger.log(Level.ERROR, String.format("Unable to create ID for file %s.", fileName));
            return;
        }
        addEquipmentDataToCache(skin, new LibraryFile(fileName));
        */
    }
    
    public void clearFileNameIdLink(LibraryFile libraryFile) {
        cacheLocalFile.remove(libraryFile);
        // TODO Tell clients that a file link id was removed.
        //MessageServerSkinIdSend message = new MessageServerSkinIdSend(libraryFile.getFullName(), 0, true);
        //PacketHandler.networkWrapper.sendToAll(message);
    }
    
    public Skin getSkin(ISkinPointer skinPointer) {
        return getSkin(skinPointer.getIdentifier());
    }
    
    public Skin getSkin(ISkinIdentifier identifier) {
        if (identifier.hasLocalId()) {
            return cacheLocalDatabase.get(identifier, false);
        } else if (identifier.hasLibraryFile()) {
            return cacheLocalFile.get(identifier, false);
        } else if (identifier.hasGlobalId()) {
             return cacheGlobal.get(identifier, false);
        } else {
            ModLogger.log(Level.ERROR, "Server requested a skin with no vaid ID:" + identifier.toString());
            return null;
        }
    }
    
    /** Returns a skin if it is in the cache. If not the skin will be loaded by another thread. */
    public Skin softGetSkin(ISkinIdentifier identifier) {
        if (identifier.hasLocalId()) {
            return cacheLocalDatabase.get(identifier, true);
        } else if (identifier.hasLibraryFile()) {
            return cacheLocalFile.get(identifier, true);
        } else if (identifier.hasGlobalId()) {
            return cacheGlobal.get(identifier, true);  
        } else {
            ModLogger.log(Level.ERROR, "Server requested a skin with no vaid ID:" + identifier.toString());
            return null;
        }
    }
    
    public int size() {
        return cacheLocalDatabase.size();
    }
    
    public int fileLinkSize() {
        return cacheLocalFile.size();
    }
    
    public int globalLinkSize() {
        return cacheGlobal.size();
    }

    @Override
    public void itemExpired(Skin mapItem) {
        if (mapItem == null) {
            return;
        }
        // TODO Work out why things are not always getting removed from the cache.
        int skinId = mapItem.lightHash();
        if (cacheLocalFile.containsValue(skinId)) {
            ILibraryFile libraryFile = cacheLocalFile.getBackward(skinId);
            cacheLocalFile.remove(libraryFile);
        }
        if (cacheGlobal.containsValue(skinId)) {
            int globalId = cacheGlobal.getBackward(skinId);
            cacheGlobal.remove(globalId);
        }
    }
}
