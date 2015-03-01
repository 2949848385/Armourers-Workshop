package riskyken.armourersWorkshop.client.render.block;

import java.awt.Color;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.client.render.MannequinFakePlayer;
import riskyken.armourersWorkshop.utils.UtilRender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockMannequinItems {
    
    private RenderPlayer renderPlayer;
    private float scale = 0.0625F;
    
    public RenderBlockMannequinItems() {
        renderPlayer = (RenderPlayer) RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
    }
    
    public void renderHeadStack(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, RenderManager rm) {
        Item targetItem = stack.getItem();
        if (targetItem instanceof ItemBlock) {
            float blockScale = 0.5F;
            GL11.glTranslatef(0, -4 * scale, 0);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedHead.rotateAngleZ), 0, 0, 1);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedHead.rotateAngleY), 0, 1, 0);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedHead.rotateAngleX), 1, 0, 0);
            
            GL11.glScalef(-blockScale, -blockScale, blockScale);
            GL11.glRotatef(90F, 0F, 1F, 0F);
            
            rm.itemRenderer.renderItem(fakePlayer, stack, 0);
        } else {
            if (targetItem instanceof ItemArmor) {
                int passes = targetItem.getRenderPasses(stack.getItemDamage());
                for (int i = 0; i < passes; i++) {
                    ModelBiped armourBiped = ForgeHooksClient.getArmorModel(fakePlayer, stack, 0, renderPlayer.modelArmorChestplate);
                    if (i == 0) {
                        bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 0, null));
                    } else {
                        bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 0, "overlay"));
                    }
                    
                    Color c = new Color(targetItem.getColorFromItemStack(stack, i));
                    GL11.glColor3f((float)c.getRed() / 255, (float)c.getGreen() / 255, (float)c.getBlue() / 255);
                    if (armourBiped == renderPlayer.modelArmorChestplate) {
                        armourBiped.bipedHead.showModel = true;
                        armourBiped.bipedHead.render(scale);
                    } else {
                        try {
                            armourBiped.render(null, 0, 0, 0, 0, 0, scale);
                        } catch (Exception e) {
                            //ModLogger.log(e);
                        }
                    }
                }
            }
        }
    }

    public void renderChestStack(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, RenderManager rm) {
        Item targetItem = stack.getItem();
        if (targetItem instanceof ItemArmor) {
            int passes = targetItem.getRenderPasses(stack.getItemDamage());
            for (int i = 0; i < passes; i++) {
                ModelBiped armourBiped = ForgeHooksClient.getArmorModel(fakePlayer, stack, 1, renderPlayer.modelArmorChestplate);
                if (i == 0) {
                    bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 1, null));
                } else {
                    bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 1, "overlay"));
                }
                
                Color c = new Color(targetItem.getColorFromItemStack(stack, i));
                GL11.glColor3f((float)c.getRed() / 255, (float)c.getGreen() / 255, (float)c.getBlue() / 255);
                
                if (armourBiped == renderPlayer.modelArmorChestplate) {
                    armourBiped.bipedBody.showModel = true;
                    armourBiped.bipedLeftArm.showModel = true;
                    armourBiped.bipedRightArm.showModel = true;
                    
                    armourBiped.bipedBody.render(scale);
                    armourBiped.bipedLeftArm.render(scale);
                    armourBiped.bipedRightArm.render(scale);
                    
                    armourBiped = ForgeHooksClient.getArmorModel(fakePlayer, stack, 1, renderPlayer.modelArmor);
                    armourBiped.bipedBody.showModel = true;
                    armourBiped.bipedBody.render(scale);
                } else {
                    try {
                        armourBiped.render(null, 0, 0, 0, 0, 0, scale);
                    } catch (Exception e) {
                        //ModLogger.log(e);
                    }
                }
            }
        }
    }
    public void renderLegsStack(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, RenderManager rm) {
        Item targetItem = stack.getItem();
        if (targetItem instanceof ItemArmor) {
            int passes = targetItem.getRenderPasses(stack.getItemDamage());
            for (int i = 0; i < passes; i++) {
                ModelBiped armourBiped = ForgeHooksClient.getArmorModel(fakePlayer, stack, 2, renderPlayer.modelArmor);
                if (i == 0) {
                    bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 2, null));
                } else {
                    bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 2, "overlay"));
                }
                
                Color c = new Color(targetItem.getColorFromItemStack(stack, i));
                GL11.glColor3f((float)c.getRed() / 255, (float)c.getGreen() / 255, (float)c.getBlue() / 255);
                
                if (armourBiped == renderPlayer.modelArmor) {
                    armourBiped.bipedBody.showModel = true;
                    armourBiped.bipedLeftLeg.showModel = true;
                    armourBiped.bipedRightLeg.showModel = true;
                    armourBiped.bipedBody.render(scale);
                    armourBiped.bipedLeftLeg.render(scale);
                    armourBiped.bipedRightLeg.render(scale);
                } else {
                    try {
                        armourBiped.render(fakePlayer, 0, 0, 0, 0, 0, scale);
                    } catch (Exception e) {
                        //ModLogger.log(e);
                    }
                }
            }
        }
    }
    
    public void renderFeetStack(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, RenderManager rm) {
        Item targetItem = stack.getItem();
        if (targetItem instanceof ItemArmor) {
            int passes = targetItem.getRenderPasses(stack.getItemDamage());
            for (int i = 0; i < passes; i++) {
                ModelBiped armourBiped = ForgeHooksClient.getArmorModel(fakePlayer, stack, 3, renderPlayer.modelArmorChestplate);
                if (i == 0) {
                    bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 3, null));
                } else {
                    bindTexture(RenderBiped.getArmorResource(fakePlayer, stack, 3, "overlay"));
                }
                
                Color c = new Color(targetItem.getColorFromItemStack(stack, i));
                GL11.glColor3f((float)c.getRed() / 255, (float)c.getGreen() / 255, (float)c.getBlue() / 255);
                
                if (armourBiped == renderPlayer.modelArmorChestplate) {
                    armourBiped.bipedLeftLeg.showModel = true;
                    armourBiped.bipedRightLeg.showModel = true;
                    armourBiped.bipedLeftLeg.render(scale);
                    armourBiped.bipedRightLeg.render(scale);
                } else {
                    try {
                        armourBiped.render(fakePlayer, 0, 0, 0, 0, 0, scale);
                    } catch (Exception e) {
                        //ModLogger.log(e);
                    }
                }
            }
        }
    }
    
    public void renderRightArmStack(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, RenderManager rm) {
        Item targetItem = stack.getItem();
        float blockScale = 0.5F;
        float itemScale = 1 - (float)1 / 3;
        Tessellator tessellator = Tessellator.instance;
        
        if (targetItem instanceof ItemBlock) {
            GL11.glTranslatef(0, -4 * scale, 0);
            GL11.glScalef(-blockScale, -blockScale, blockScale);
            GL11.glRotatef(90F, 0F, 1F, 0F);
        } else {
            //Movement
            GL11.glTranslatef(-5F * scale, 0F, 0F);
            GL11.glTranslatef(0F, 2F * scale, 0F);
            
            GL11.glRotated(Math.toDegrees(targetBiped.bipedRightArm.rotateAngleZ), 0F, 0F, 1F);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedRightArm.rotateAngleY), 0F, 1F, 0F);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedRightArm.rotateAngleX), 1F, 0F, 0F);
            
            GL11.glTranslatef(-2F * scale, 0F, 0F);
            GL11.glTranslatef(0F, 10F * scale, 0F);
            
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glRotatef(45, 0, 0, 1);
            
            GL11.glScalef(itemScale, itemScale, itemScale);
            GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
        }
        
        rm.itemRenderer.renderItem(fakePlayer, stack, 0, ItemRenderType.EQUIPPED);
    }
    
    public void renderLeftArmStack(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, RenderManager rm) {
        Item targetItem = stack.getItem();
        float blockScale = 0.5F;
        float itemScale = 1 - (float)1 / 3;
        
        if (targetItem instanceof ItemBlock) {
            GL11.glTranslatef(0, -4 * scale, 0);
            GL11.glScalef(-blockScale, -blockScale, blockScale);
            GL11.glRotatef(90F, 0F, 1F, 0F);
        } else {
            //Movement
            GL11.glTranslatef(5F * scale, 0F, 0F);
            GL11.glTranslatef(0F, 2F * scale, 0F);
            
            GL11.glRotated(Math.toDegrees(targetBiped.bipedLeftArm.rotateAngleZ), 0F, 0F, 1F);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedLeftArm.rotateAngleY), 0F, 1F, 0F);
            GL11.glRotated(Math.toDegrees(targetBiped.bipedLeftArm.rotateAngleX), 1F, 0F, 0F);
            
            GL11.glTranslatef(1F * scale, 0F, 0F);
            GL11.glTranslatef(0F, 10F * scale, 0F);
            
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glRotatef(45, 0, 0, 1);
            
            GL11.glScalef(itemScale, itemScale, itemScale);
            GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
        }

        rm.itemRenderer.renderItem(fakePlayer, stack, 0, ItemRenderType.EQUIPPED);
    }
    
    private void bindTexture(ResourceLocation resourceLocation) {
        UtilRender.bindTexture(resourceLocation);
    }
}
