package riskyken.armourersWorkshop.client.gui.globallibrary;

import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.lwjgl.opengl.GL11;

import com.google.gson.JsonArray;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import riskyken.armourersWorkshop.client.gui.controls.GuiPanel;
import riskyken.armourersWorkshop.client.model.bake.ModelBakery;
import riskyken.armourersWorkshop.client.render.ItemStackRenderHelper;
import riskyken.armourersWorkshop.client.render.ModRenderHelper;
import riskyken.armourersWorkshop.client.skin.cache.ClientSkinCache;
import riskyken.armourersWorkshop.common.library.global.SkinDownloader;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPointer;

public class GuiGlobalLibraryPanelSearchResults extends GuiPanel {
    
    private ArrayList<SkinPointer> skins = new ArrayList<SkinPointer>();
    private int page = 0;
    private int displayCount = 1;
    private FutureTask<JsonArray> downloadSearchResultsTask;
    private CompletionService<Skin> skinCompletion;
    
    public GuiGlobalLibraryPanelSearchResults(GuiScreen parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        skinCompletion = new ExecutorCompletionService<Skin>(((GuiGlobalLibrary)parent).skinDownloadExecutor);
    }
    
    public void setDownloadSearchResultsTask(FutureTask<JsonArray> downloadSearchResultsTask) {
        this.downloadSearchResultsTask = downloadSearchResultsTask;
    }
    
    @Override
    public void updatePanel() {
        if (downloadSearchResultsTask != null && downloadSearchResultsTask.isDone()) {
            try {
                clearSkins();
                JsonArray json = downloadSearchResultsTask.get();
                SkinDownloader.downloadSkins(skinCompletion, json);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            downloadSearchResultsTask = null;
        }
        
        Future<Skin> futureSkin = skinCompletion.poll();
        if (futureSkin != null) {
            try {
                Skin skin = futureSkin.get();
                if (skin != null) {
                    SkinPointer skinPointer = new SkinPointer(skin);
                    skins.add(skinPointer);
                    if (skin != null && !ClientSkinCache.INSTANCE.isSkinInCache(skinPointer)) {
                        ModelBakery.INSTANCE.receivedUnbakedModel(skin);
                    } else {
                        if (skin != null) {
                            ClientSkinCache.INSTANCE.addServerIdMap(skin);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    
    public void clearSkins() {
        skins.clear();
        page = 0;
    }
}
