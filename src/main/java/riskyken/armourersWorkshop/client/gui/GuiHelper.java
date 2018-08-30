package riskyken.armourersWorkshop.client.gui;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import riskyken.armourersWorkshop.client.texture.PlayerTexture;
import riskyken.armourersWorkshop.common.data.TextureType;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.proxies.ClientProxy;

@SideOnly(Side.CLIENT)
public final class GuiHelper {
    
    private GuiHelper() {}
    
    public static void drawPlayerHead(int x, int y, int size, String username) {
        
        ResourceLocation rl = DefaultPlayerSkin.getDefaultSkinLegacy();
        if (!StringUtils.isNullOrEmpty(username)) {
            PlayerTexture playerTexture = getPlayerTexture(username, TextureType.USER);
            rl = playerTexture.getResourceLocation();  
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(rl);
        
        int sourceSize = 8;
        
        // Face
        Gui.drawScaledCustomSizeModalRect(x + 1, y + 1, 8, 8, sourceSize, sourceSize, size, size, 64, 32);
        // Overlay
        Gui.drawScaledCustomSizeModalRect(x, y, 40, 8, sourceSize, sourceSize, size + 2, size + 2, 64, 32);
    }
    
    private static PlayerTexture getPlayerTexture(String textureString, TextureType textureType) {
        return ClientProxy.playerTextureDownloader.getPlayerTexture(textureString, textureType);
    }
    
    public static void renderLocalizedGuiName(FontRenderer fontRenderer, int xSize, String name) {
        renderLocalizedGuiName(fontRenderer, xSize, name, null, 4210752);
    }
    
    public static void renderLocalizedGuiName(FontRenderer fontRenderer, int xSize, String name, int colour) {
        renderLocalizedGuiName(fontRenderer, xSize, name, null, colour);
    }
    
    public static void renderLocalizedGuiName(FontRenderer fontRenderer, int xSize, String name, String append) {
        renderLocalizedGuiName(fontRenderer, xSize, name, append, 4210752);
    }
    
    public static void renderLocalizedGuiName(FontRenderer fontRenderer, int xSize, String name, String append, int colour) {
        String unlocalizedName = "inventory." + LibModInfo.ID.toLowerCase() + ":" + name + ".name";
        String localizedName = I18n.format(unlocalizedName);
        String renderText = unlocalizedName;
        if (!unlocalizedName.equals(localizedName)){
            renderText = localizedName;
        }
        if (append != null) {
            renderText = renderText + " - " + append;
        }
        int xPos = xSize / 2 - fontRenderer.getStringWidth(renderText) / 2;
        fontRenderer.drawString(renderText, xPos, 6, colour);
    }
    
    public static String getLocalizedControlName(String guiName, String controlName) {
        String unlocalizedName = "inventory." + LibModInfo.ID.toLowerCase() + ":" + guiName + "." + controlName;
        String localizedName = I18n.format(unlocalizedName);
        if (!unlocalizedName.equals(localizedName)){
            return localizedName;
        }
        return unlocalizedName;
    }
    
    public static void drawHoveringText(List textList, int xPos, int yPos, FontRenderer font, int width, int height, float zLevel) {
        if (!textList.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            //RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int textWidth = 0;
            Iterator iterator = textList.iterator();

            while (iterator.hasNext()) {
                String line = (String)iterator.next();
                int sWidth = font.getStringWidth(line);
                if (sWidth > textWidth) {
                    textWidth = sWidth;
                }
            }

            int renderX = xPos + 12;
            int renderY = yPos - 12;
            int textHeight = 8;

            if (textList.size() > 1) {
                textHeight += 2 + (textList.size() - 1) * 10;
            }

            if (renderX + textWidth > width - 2) {
                renderX -= 28 + textWidth;
            }

            if (renderY + textHeight + 6 > height) {
                renderY = height - textHeight - 6;
            }
            
            if (renderY < 5) {
                renderY = 5;
            }

            zLevel = 300.0F;
            int j1 = -267386864;
            drawGradientRect(renderX - 3, renderY - 4, renderX + textWidth + 3, renderY - 3, j1, j1, zLevel);
            drawGradientRect(renderX - 3, renderY + textHeight + 3, renderX + textWidth + 3, renderY + textHeight + 4, j1, j1, zLevel);
            drawGradientRect(renderX - 3, renderY - 3, renderX + textWidth + 3, renderY + textHeight + 3, j1, j1, zLevel);
            drawGradientRect(renderX - 4, renderY - 3, renderX - 3, renderY + textHeight + 3, j1, j1, zLevel);
            drawGradientRect(renderX + textWidth + 3, renderY - 3, renderX + textWidth + 4, renderY + textHeight + 3, j1, j1, zLevel);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(renderX - 3, renderY - 3 + 1, renderX - 3 + 1, renderY + textHeight + 3 - 1, k1, l1, zLevel);
            drawGradientRect(renderX + textWidth + 2, renderY - 3 + 1, renderX + textWidth + 3, renderY + textHeight + 3 - 1, k1, l1, zLevel);
            drawGradientRect(renderX - 3, renderY - 3, renderX + textWidth + 3, renderY - 3 + 1, k1, k1, zLevel);
            drawGradientRect(renderX - 3, renderY + textHeight + 2, renderX + textWidth + 3, renderY + textHeight + 3, l1, l1, zLevel);

            for (int i2 = 0; i2 < textList.size(); ++i2) {
                String line = (String)textList.get(i2);
                font.drawStringWithShadow(line, renderX, renderY, -1);
                if (i2 == 0) {
                    renderY += 2;
                }
                renderY += 10;
            }

            zLevel = 0.0F;
            //GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    
    private static void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_, float zLevel)
    {
        float f = (float)(p_73733_5_ >> 24 & 255) / 255.0F;
        float f1 = (float)(p_73733_5_ >> 16 & 255) / 255.0F;
        float f2 = (float)(p_73733_5_ >> 8 & 255) / 255.0F;
        float f3 = (float)(p_73733_5_ & 255) / 255.0F;
        float f4 = (float)(p_73733_6_ >> 24 & 255) / 255.0F;
        float f5 = (float)(p_73733_6_ >> 16 & 255) / 255.0F;
        float f6 = (float)(p_73733_6_ >> 8 & 255) / 255.0F;
        float f7 = (float)(p_73733_6_ & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double)p_73733_3_, (double)p_73733_2_, (double)zLevel);
        tessellator.addVertex((double)p_73733_1_, (double)p_73733_2_, (double)zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)p_73733_1_, (double)p_73733_4_, (double)zLevel);
        tessellator.addVertex((double)p_73733_3_, (double)p_73733_4_, (double)zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
