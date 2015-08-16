package riskyken.armourersWorkshop.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.client.ModClientFMLEventHandler;
import riskyken.armourersWorkshop.client.model.ClientModelCache;
import riskyken.armourersWorkshop.client.model.bake.ModelBakery;
import riskyken.armourersWorkshop.client.render.EquipmentModelRenderer;
import riskyken.armourersWorkshop.common.config.ConfigHandler;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

@SideOnly(Side.CLIENT)
public class DebugTextHandler {
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDebugText(RenderGameOverlayEvent.Text event) {
        if (!ConfigHandler.showF3DebugInfo) {
            return;
        }
        if (event.left != null && event.left.size() > 0) {
            event.left.add("");
            event.left.add(EnumChatFormatting.GOLD + "[" + LibModInfo.NAME + "]");
            String dataLine = "";
            dataLine += "mc:" + ArmourersWorkshop.proxy.getPlayerModelCacheSize() + " - ";
            dataLine += "pd:" + EquipmentModelRenderer.INSTANCE.getSkinDataMapSize() + " - ";
            dataLine += "bq:" + ModelBakery.INSTANCE.getBakingQueueSize() + " - ";
            dataLine += "rq:" + ClientModelCache.INSTANCE.getRequestQueueSize();
            event.left.add(dataLine);
            dataLine = "sr:" + ModClientFMLEventHandler.skinRenderLastTick;
            event.left.add(dataLine);
        }
    }
}
