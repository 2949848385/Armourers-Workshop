package riskyken.armourersWorkshop.client.gui.globallibrary;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.google.gson.JsonArray;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import riskyken.armourersWorkshop.client.gui.controls.GuiPanel;
import riskyken.armourersWorkshop.client.model.bake.ModelBakery;
import riskyken.armourersWorkshop.client.render.ItemStackRenderHelper;
import riskyken.armourersWorkshop.client.render.ModRenderHelper;
import riskyken.armourersWorkshop.client.skin.ClientSkinCache;
import riskyken.armourersWorkshop.common.library.global.SkinDownloader;
import riskyken.armourersWorkshop.common.library.global.SkinDownloader.IDownloadListCallback;
import riskyken.armourersWorkshop.common.library.global.SkinDownloader.IDownloadSkinCallback;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPointer;
import riskyken.armourersWorkshop.utils.ModLogger;

public class GuiGlobalLibraryPanelSearchResults extends GuiPanel implements IDownloadListCallback, IDownloadSkinCallback {
    
    private ArrayList<SkinPointer> skins = new ArrayList<SkinPointer>();
    private int page = 0;
    private int displayCount = 1;
    
    public GuiGlobalLibraryPanelSearchResults(GuiScreen parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
    }
    
    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButtonExt(0, x + 5, y + height - 25, 80, 20, "<<"));
        buttonList.add(new GuiButtonExt(1, x + width - 85, y + height - 25, 80, 20, ">>"));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        int skinCount = 0;
        synchronized (skins) {
            skinCount = skins.size();
        }
        int maxPages = (int) Math.ceil((float)skinCount / (float)displayCount);
        
        if (button.id == 0) {
            if (page > 0) {
                page--;
            }
        }
        if (button.id == 1) {
            if (page < maxPages - 1) {
                page++;
            }
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTickTime) {
        if (!visible) {
            return;
        }
        drawGradientRect(this.x, this.y, this.x + this.width, this.y + height, 0xC0101010, 0xD0101010);
        super.drawScreen(mouseX, mouseY, partialTickTime);
        
        int boxW = width - 5;
        int boxH = height - 5 - 12;
        int iconSize = 110;
        synchronized (skins) {
            int maxPages = (int) Math.ceil((float)skins.size() / (float)displayCount);
            fontRenderer.drawString("Search Results:  Page " + (page + 1) + " of " + (maxPages) + " - Total of " + skins.size() + " Results", x + 5, y + 6, 0xFFEEEEEE);
            
            //ModLogger.log(maxPages);
            for (int i = page * displayCount; i < skins.size(); i++) {
                int rowSize = (int) Math.floor(boxW / iconSize);
                int colSize = (int) Math.floor(boxH / iconSize);
                displayCount = rowSize * colSize;
                int x = (i - page * displayCount) % rowSize;
                int y = (i - page * displayCount) / rowSize;
                
                SkinPointer skinPointer = skins.get(i);
                Skin skin = ClientSkinCache.INSTANCE.getSkin(skinPointer, false);
                if (skin != null) {
                    float scale = iconSize / 3;
                    if (y < colSize) {
                        int size = fontRenderer.getStringWidth(skin.getCustomName());
                        fontRenderer.drawString(skin.getCustomName(), (int) (this.x + x * iconSize + iconSize / 2 - size / 2), this.y + y * iconSize + iconSize, 0xFFEEEEEE);
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
                        GL11.glTranslatef(this.x + iconSize / 2 + x * iconSize, 12 + this.y + iconSize / 2 + y * iconSize, 200.0F);
                        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
                        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                        float rotation = (float)((double)System.currentTimeMillis() / 10 % 360);
                        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
                        RenderHelper.enableStandardItemLighting();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glEnable(GL11.GL_NORMALIZE);
                        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                        ModRenderHelper.enableAlphaBlend();
                        ItemStackRenderHelper.renderItemModelFromSkin(skin, skinPointer, true, false);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }
    
    public void clearSkin() {
        synchronized (skins) {
            skins.clear();
            page = 0;
        }
    }
    
    @Override
    public void skinDownloaded(Skin skin, SkinPointer skinPointer) {
        synchronized (skins) {
            skins.add(skinPointer);
        }
        if (skin != null && !ClientSkinCache.INSTANCE.isSkinInCache(skinPointer)) {
            ModelBakery.INSTANCE.receivedUnbakedModel(skin);
        } else {
            ModLogger.log("Model was already downloaded.");
        }
    }

    @Override
    public void listDownloadFinished(JsonArray json) {
        SkinDownloader.downloadSkins(this, json);
    }
}
