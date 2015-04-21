package riskyken.armourersWorkshop.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.client.model.ModelHelper;
import riskyken.armourersWorkshop.client.model.ModelMannequin;
import riskyken.armourersWorkshop.client.render.EquipmentModelRenderer;
import riskyken.armourersWorkshop.client.render.MannequinFakePlayer;
import riskyken.armourersWorkshop.common.ApiRegistrar;
import riskyken.armourersWorkshop.common.SkinHelper;
import riskyken.armourersWorkshop.common.inventory.MannequinSlotType;
import riskyken.armourersWorkshop.common.items.ModItems;
import riskyken.armourersWorkshop.common.tileentities.TileEntityMannequin;
import riskyken.armourersWorkshop.utils.HolidayHelper;
import riskyken.armourersWorkshop.utils.HolidayHelper.EnumHoliday;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockMannequin extends TileEntitySpecialRenderer {
    
    private static RenderBlockMannequinItems renderItems = new RenderBlockMannequinItems();
    private static boolean isHalloween;
    private ModelMannequin model;
    private RenderPlayer renderPlayer;
    private final Minecraft mc;
    private final float SCALE = 0.0625F;
    
    public RenderBlockMannequin() {
        renderPlayer = (RenderPlayer) RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
        mc = Minecraft.getMinecraft();
        model = new ModelMannequin();
        isHalloween = HolidayHelper.getHoliday(1) == EnumHoliday.HALLOWEEN;
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tickTime) {
        mc.mcProfiler.startSection("armourers mannequin");
        TileEntityMannequin te = (TileEntityMannequin) tileEntity;
        MannequinFakePlayer fakePlayer = te.getFakePlayer();
        
        double heightOffset = te.getHeightOffset();
        //heightOffset = 12;
        ModelBiped targetBiped = renderPlayer.modelBipedMain;
        if (Loader.isModLoaded("moreplayermodels")) {
            targetBiped = model;
        }
        
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_NORMALIZE);
        
        int rotaion = te.getRotation();
        
        GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
        GL11.glScalef(SCALE * 15, SCALE * 15, SCALE * 15);
        GL11.glTranslated(0, SCALE * -1.6F, 0);
        
        GL11.glScalef(-1, -1, 1);
        GL11.glRotatef(rotaion * 22.5F, 0, 1, 0);
        
        if (te.getIsDoll()) {
            float dollScale = 0.5F;
            GL11.glScalef(dollScale, dollScale, dollScale);
            GL11.glTranslatef(0, SCALE * 24, 0);
        }
        
        if (te.getGameProfile() != null) {
            if (te.getGameProfile() != null & te.getWorldObj() != null) {
                if (fakePlayer == null) {
                    WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(te.getWorldObj().provider.dimensionId);
                    fakePlayer = new MannequinFakePlayer(worldServer, te.getGameProfile());
                    fakePlayer.posX = x;
                    fakePlayer.posY = y;
                    fakePlayer.posZ = z;
                    fakePlayer.prevPosX = x;
                    fakePlayer.prevPosY = y;
                    fakePlayer.prevPosZ = z;
                    
                    te.setFakePlayer(fakePlayer);
                }
            }
        }
        
        float f6 = 2.0F;
        if (fakePlayer != null) {
            targetBiped.isChild = te.getBipedRotations().isChild;
            fakePlayer.rotationPitch = (float) Math.toDegrees(te.getBipedRotations().head.rotationX);
            fakePlayer.prevRotationPitch = (float) Math.toDegrees(te.getBipedRotations().head.rotationX);
            fakePlayer.rotationYawHead = (float) Math.toDegrees(te.getBipedRotations().head.rotationY);
            fakePlayer.prevRotationYawHead = (float) Math.toDegrees(te.getBipedRotations().head.rotationY);
            fakePlayer.onUpdate();
            
            fakePlayer.ticksExisted = Minecraft.getMinecraft().thePlayer.ticksExisted;
            
            if (te.getBipedRotations() != null) {
                te.getBipedRotations().applyRotationsToBiped(targetBiped);
                te.getBipedRotations().applyRotationsToBiped(renderPlayer.modelArmor);
                te.getBipedRotations().applyRotationsToBiped(renderPlayer.modelArmorChestplate);
            }
            
            //Pre render events
            RenderPlayerEvent.Pre preEvent = new RenderPlayerEvent.Pre(fakePlayer, renderPlayer, tickTime);
            RenderPlayerEvent.Specials.Pre preEventSpecials = new RenderPlayerEvent.Specials.Pre(fakePlayer, renderPlayer, tickTime);

            if (targetBiped.isChild) {
                GL11.glPushMatrix();
                GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
                GL11.glTranslatef(0.0F, 16.0F * SCALE, 0.0F);
            }
            GL11.glDisable(GL11.GL_CULL_FACE);
            MinecraftForge.EVENT_BUS.post(preEvent);
            MinecraftForge.EVENT_BUS.post(preEventSpecials);
            GL11.glEnable(GL11.GL_CULL_FACE);
            if (targetBiped.isChild) {
                GL11.glPopMatrix();
            }
        }
        
        
        ApiRegistrar.INSTANCE.onRenderMannequin(tileEntity, te.getGameProfile());
        
        targetBiped.bipedRightArm.setRotationPoint(-5.0F, 2.0F , 0.0F);
        targetBiped.bipedLeftArm.setRotationPoint(5.0F, 2.0F , 0.0F);
        targetBiped.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        targetBiped.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        targetBiped.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        targetBiped.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        
        te.getBipedRotations().applyRotationsToBiped(targetBiped);
        
        //TODO Fade in skins
        /*
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(AbstractClientPlayer.locationStevePng);
        renderModel(te, targetBiped, fakePlayer);
        GL11.glPopMatrix();
        
        float alpha = (float)((double)System.currentTimeMillis() / 5000 % 2);
        if (alpha > 1F) {
            alpha -= 1F;
            alpha = 1 - alpha;
        }
        
        ModRenderHelper.enableAlphaBlend();
        //GL11.glAlphaFunc(GL11.GL_GREATER, 0F);
        GL11.glColor4f(1F, 1F, 1F, alpha);
        SkinHelper.bindPlayersNormalSkin(te.getGameProfile());
        renderModel(te, targetBiped, fakePlayer);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        ModRenderHelper.disableAlphaBlend();
        */
        
        //Render model
        SkinHelper.bindPlayersNormalSkin(te.getGameProfile());
        renderModel(te, targetBiped, fakePlayer);
        
        
        //Render items.
        renderEquippedItems(te, fakePlayer, targetBiped);
        
        //Post render events
        if (fakePlayer != null) {
            RenderPlayerEvent.Post postEvent = new RenderPlayerEvent.Post(fakePlayer, renderPlayer, tickTime);
            RenderPlayerEvent.Specials.Post postEvenSpecialst = new RenderPlayerEvent.Specials.Post(fakePlayer, renderPlayer, tickTime);
            if (targetBiped.isChild) {
                GL11.glPushMatrix();
                GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
                GL11.glTranslatef(0.0F, 16.0F * SCALE, 0.0F);
            }
            GL11.glDisable(GL11.GL_CULL_FACE);
            MinecraftForge.EVENT_BUS.post(postEvent);
            MinecraftForge.EVENT_BUS.post(postEvenSpecialst);
            GL11.glEnable(GL11.GL_CULL_FACE);
            if (targetBiped.isChild) {
                GL11.glPopMatrix();
            }
        }
        
        //Render armourer's skins
        mc.mcProfiler.startSection("mannequin skin");
        EquipmentModelRenderer.INSTANCE.renderMannequinEquipment(((TileEntityMannequin)tileEntity), targetBiped);
        mc.mcProfiler.endSection();
        
        targetBiped.bipedLeftLeg.rotateAngleZ = 0F;
        targetBiped.bipedRightLeg.rotateAngleZ = 0F;
        targetBiped.bipedHead.rotateAngleZ = 0F;
        targetBiped.bipedHeadwear.rotateAngleZ = 0F;
        
        GL11.glDisable(GL11.GL_NORMALIZE);
        GL11.glPopMatrix();
        mc.mcProfiler.endSection();
    }
    
    private void renderModel(TileEntityMannequin te, ModelBiped targetBiped, MannequinFakePlayer fakePlayer) {
        if (!hasCustomHead(te, fakePlayer)) {
            if (te.getBipedRotations().isChild) {
                ModelHelper.enableChildModelScale(true, SCALE);
            }
            targetBiped.bipedHead.render(SCALE);
            GL11.glDisable(GL11.GL_CULL_FACE);
            targetBiped.bipedHeadwear.render(SCALE);
            GL11.glEnable(GL11.GL_CULL_FACE);
            if (te.getBipedRotations().isChild) {
                ModelHelper.disableChildModelScale();
            }
        }
        if (te.getBipedRotations().isChild) {
            ModelHelper.enableChildModelScale(false, SCALE);
        }
        targetBiped.bipedBody.render(SCALE);
        targetBiped.bipedRightArm.render(SCALE);
        targetBiped.bipedLeftArm.render(SCALE);
        targetBiped.bipedRightLeg.render(SCALE);
        targetBiped.bipedLeftLeg.render(SCALE);
        if (te.getBipedRotations().isChild) {
            ModelHelper.disableChildModelScale();
        }
    }
    
    private void renderEquippedItems(IInventory inventory, MannequinFakePlayer fakePlayer, ModelBiped targetBiped) {
        RenderItem ri = (RenderItem) RenderManager.instance.entityRenderMap.get(EntityItem.class);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (fakePlayer != null) {
                if (i == 0 & isHalloween) {
                    renderEquippedItem(fakePlayer, new ItemStack(Blocks.lit_pumpkin), targetBiped, i);
                } else {
                    if (stack != null) {
                        renderEquippedItem(fakePlayer, stack, targetBiped, i);
                    }
                }
            }
        }
    }
    
    public ItemStack getStackInMannequinSlot(IInventory inventory, MannequinSlotType slot) {
        return inventory.getStackInSlot(slot.ordinal());
    }
    
    private boolean hasCustomHead(IInventory inventory, MannequinFakePlayer fakePlayer) {
        ItemStack stack = getStackInMannequinSlot(inventory, MannequinSlotType.HEAD);
        if (stack != null & fakePlayer != null) {
            if (stack.getItem() instanceof ItemBlock) {
                return true;
            }
        }
        if (isHalloween) {
            return true;
        }
        return false;
    }
    
    private void renderEquippedItem(MannequinFakePlayer fakePlayer, ItemStack stack, ModelBiped targetBiped, int slot) {
        if (slot < 7) {
            if (stack.getItem() == ModItems.equipmentSkin) {
                return;
            }
        }
        
        Item targetItem = stack.getItem();
        RenderManager rm = RenderManager.instance;
        
        GL11.glPushMatrix();
        if (targetBiped.isChild) {
            ModelHelper.enableChildModelScale(slot == 0, SCALE);
        }
        switch (slot) {
        case 0:
            renderItems.renderHeadStack(fakePlayer, stack, targetBiped, rm);
            break;
        case 1:
            renderItems.renderChestStack(fakePlayer, stack, targetBiped, rm);
            break;
        case 2:
            renderItems.renderLegsStack(fakePlayer, stack, targetBiped, rm);
            break;
        case 4:
            renderItems.renderFeetStack(fakePlayer, stack, targetBiped, rm);
            break;
        case 5:
            renderItems.renderRightArmStack(fakePlayer, stack, targetBiped, rm);
            break;
        case 6:
            renderItems.renderLeftArmStack(fakePlayer, stack, targetBiped, rm);
            break;
        }
        if (targetBiped.isChild) {
            ModelHelper.disableChildModelScale();
        }
        GL11.glPopMatrix();
    }
}
