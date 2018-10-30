package moe.plushie.armourers_workshop.common.capability;

import java.util.concurrent.Callable;

import moe.plushie.armourers_workshop.api.common.skin.entity.ISkinnableEntity;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinProvider;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinStorage;
import moe.plushie.armourers_workshop.common.capability.entityskin.IEntitySkinCapability;
import moe.plushie.armourers_workshop.common.capability.holiday.HolidayTrackCap;
import moe.plushie.armourers_workshop.common.capability.holiday.IHolidayTrackCap;
import moe.plushie.armourers_workshop.common.capability.wardrobe.IWardrobeCap;
import moe.plushie.armourers_workshop.common.capability.wardrobe.WardrobeProvider;
import moe.plushie.armourers_workshop.common.capability.wardrobe.WardrobeStorage;
import moe.plushie.armourers_workshop.common.capability.wardrobe.player.IPlayerWardrobeCap;
import moe.plushie.armourers_workshop.common.capability.wardrobe.player.PlayerWardrobeProvider;
import moe.plushie.armourers_workshop.common.capability.wardrobe.player.PlayerWardrobeStorage;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.common.skin.entity.EntitySkinHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibModInfo.ID)
public final class ModCapabilityManager {

    private static final ResourceLocation KEY_ENTITY_SKIN_PROVIDER = new ResourceLocation(LibModInfo.ID, "entity-skin-provider");
    private static final ResourceLocation KEY_WARDROBE_PROVIDER = new ResourceLocation(LibModInfo.ID, "wardrobe-provider");
    private static final ResourceLocation KEY_PLAYER_WARDROBE_PROVIDER = new ResourceLocation(LibModInfo.ID, "player-wardrobe-provider");
    private static final ResourceLocation KEY_HOLIDAY_TRACKER = new ResourceLocation(LibModInfo.ID, "holiday-tracker");
    
    private ModCapabilityManager() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IEntitySkinCapability.class, new EntitySkinStorage(), new Callable<IEntitySkinCapability>() {

            @Override
            public IEntitySkinCapability call() throws Exception {
                return null;
            }
        });

        CapabilityManager.INSTANCE.register(IWardrobeCap.class, new WardrobeStorage(), new Callable<IWardrobeCap>() {

            @Override
            public IWardrobeCap call() throws Exception {
                return null;
            }
        });
        
        CapabilityManager.INSTANCE.register(IPlayerWardrobeCap.class, new PlayerWardrobeStorage(), new Callable<IPlayerWardrobeCap>() {

            @Override
            public IPlayerWardrobeCap call() throws Exception {
                return null;
            }
        });
        CapabilityManager.INSTANCE.register(IHolidayTrackCap.class, new HolidayTrackCap.Storage(), new HolidayTrackCap.Factory());
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase entityLivingBase = (EntityLivingBase) event.getObject();
        ISkinnableEntity skinnableEntity = EntitySkinHandler.INSTANCE.getSkinnableEntity(entityLivingBase);
        if (skinnableEntity == null) {
            return;
        }
        event.addCapability(KEY_ENTITY_SKIN_PROVIDER, new EntitySkinProvider(entityLivingBase, skinnableEntity));
        if (entityLivingBase instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBase;
            event.addCapability(KEY_PLAYER_WARDROBE_PROVIDER, new PlayerWardrobeProvider(player, skinnableEntity));
            event.addCapability(KEY_HOLIDAY_TRACKER, new HolidayTrackCap.Provider());
        } else {
            event.addCapability(KEY_WARDROBE_PROVIDER, new WardrobeProvider(entityLivingBase, skinnableEntity));
        }
    }
}
