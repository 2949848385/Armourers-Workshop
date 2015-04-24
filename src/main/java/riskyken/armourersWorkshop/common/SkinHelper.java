package riskyken.armourersWorkshop.common;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;

import riskyken.armourersWorkshop.client.handler.PlayerSkinHandler;
import riskyken.armourersWorkshop.client.render.PlayerSkinInfo;
import riskyken.armourersWorkshop.common.data.PlayerPointer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

public final class SkinHelper {

    public static void uploadTexture(ResourceLocation resourceLocation, BufferedImage bufferedImage) {
        ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation);
        if (textureObject != null & bufferedImage != null) {
            uploadTexture(textureObject, bufferedImage);
        }
    }
    
    private static void uploadTexture(ITextureObject textureObject, BufferedImage bufferedImage) {
        TextureUtil.uploadTextureImage(textureObject.getGlTextureId(), bufferedImage);
    }

    /*
     * Based on @KitsuneKihira texture helper class.
     * https://github.com/kihira/FoxLib/blob/2946cd6033d3039151064ceccfb8d38612d0af02/src/main/scala/kihira/foxlib/client/TextureHelper.scala#L28
     */
    
    public static BufferedImage getBufferedImageSkin(AbstractClientPlayer player) {
        BufferedImage bufferedImage = null;
        ResourceLocation skinloc = AbstractClientPlayer.locationStevePng;
        InputStream inputStream = null;
        Minecraft mc = Minecraft.getMinecraft();
        skinloc = player.getLocationSkin();
        try {
            ITextureObject skintex = mc.getTextureManager().getTexture(skinloc);
            if (skintex instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData imageData = (ThreadDownloadImageData)skintex;
                bufferedImage  = ObfuscationReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, imageData, "bufferedImage", "field_110560_d", "bpr.h");
            } else {
                inputStream = Minecraft.getMinecraft().getResourceManager().getResource(skinloc).getInputStream();
                bufferedImage = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return bufferedImage;
    }
    
    public static BufferedImage getBufferedImageSkin(GameProfile gameProfile) {
        BufferedImage bufferedImage = null;
        ResourceLocation skinloc = AbstractClientPlayer.locationStevePng;
        InputStream inputStream = null;
        Minecraft mc = Minecraft.getMinecraft();
        Map map = mc.func_152342_ad().func_152788_a(gameProfile);
        
        try {
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                skinloc = mc.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
                ITextureObject skintex = mc.getTextureManager().getTexture(skinloc);
                
                if (skintex instanceof ThreadDownloadImageData) {
                    ThreadDownloadImageData imageData = (ThreadDownloadImageData)skintex;
                    bufferedImage  = ObfuscationReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, imageData, "bufferedImage", "field_110560_d", "bpr.h");
                } else {
                    inputStream = Minecraft.getMinecraft().getResourceManager().getResource(skinloc).getInputStream();
                    bufferedImage = ImageIO.read(inputStream);
                }
            } else {
                //inputStream = Minecraft.getMinecraft().getResourceManager().getResource(skinloc).getInputStream();
                //bufferedImage = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        
        return bufferedImage;
    }
    
    public static void bindPlayersNormalSkin(GameProfile gameProfile) {
        ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
        PlayerSkinInfo skinInfo = null;
        if (gameProfile != null) {
            PlayerPointer playerPointer = new PlayerPointer(gameProfile);
            skinInfo = PlayerSkinHandler.INSTANCE.getPlayersNakedData(playerPointer);
            resourcelocation = getSkinResourceLocation(gameProfile, MinecraftProfileTexture.Type.SKIN);
        }
        
        if (skinInfo != null) {
            if (skinInfo.getNakedInfo().isNaked) {
                if (!skinInfo.bindNomalSkin()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(resourcelocation);
                }
            } else {
                Minecraft.getMinecraft().getTextureManager().bindTexture(resourcelocation);
            }
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourcelocation);
        }
    }
    
    public static ResourceLocation getSkinResourceLocation(GameProfile gameProfile, MinecraftProfileTexture.Type type) {
        ResourceLocation skin = AbstractClientPlayer.locationStevePng;
        Minecraft mc = Minecraft.getMinecraft();
        Map<?, ?> map = mc.func_152342_ad().func_152788_a(gameProfile);
        if (map.containsKey(type)) {
            skin = mc.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(type), type);
        }
        return skin;
    }
    
    public static BufferedImage deepCopyBufferedImage(BufferedImage bufferedImage) {
        ColorModel cm = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
