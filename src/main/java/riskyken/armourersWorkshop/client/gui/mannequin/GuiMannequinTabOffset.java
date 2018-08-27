package riskyken.armourersWorkshop.client.gui.mannequin;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import riskyken.armourersWorkshop.client.gui.GuiHelper;
import riskyken.armourersWorkshop.client.gui.controls.GuiCustomSlider;
import riskyken.armourersWorkshop.client.gui.controls.GuiTabPanel;
import riskyken.armourersWorkshop.common.data.Rectangle_I_2D;
import riskyken.armourersWorkshop.common.data.TextureType;
import riskyken.armourersWorkshop.common.network.PacketHandler;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiMannequinData;
import riskyken.armourersWorkshop.common.tileentities.TileEntityMannequin;

@SideOnly(Side.CLIENT)
public class GuiMannequinTabOffset extends GuiTabPanel implements ISlider {
    
    private static final int TAB_WIDTH = 216;
    private static final int TAB_HEIGHT = 90;
    
    private final String inventoryName;
    private final TileEntityMannequin tileEntity;
    
    private boolean guiLoaded = false;
    private GuiButtonExt resetOffsetButton;
    private GuiCustomSlider bipedOffsetXslider;
    private GuiCustomSlider bipedOffsetYslider;
    private GuiCustomSlider bipedOffsetZslider;
    
    public GuiMannequinTabOffset(int tabId, GuiScreen parent, String inventoryName, TileEntityMannequin tileEntity) {
        super(tabId, parent, true);
        this.inventoryName = inventoryName;
        this.tileEntity = tileEntity;
    }
    
    @Override
    public void initGui(int xPos, int yPos, int width, int height) {
        super.initGui(xPos, yPos, width, height);
        guiLoaded = false;
        resetOffsetButton = new GuiButtonExt(0, 0, TAB_HEIGHT - 18 - 8, 50, 18, GuiHelper.getLocalizedControlName(inventoryName, "reset"));
        resetOffsetButton.width = fontRenderer.getStringWidth(resetOffsetButton.displayString) + fontRenderer.getStringWidth(" ") * 4;
        resetOffsetButton.xPosition = this.width / 2  - TAB_WIDTH / 2 + TAB_WIDTH - 10 - resetOffsetButton.width;
        bipedOffsetXslider = new GuiCustomSlider(0, (int)((width / 2F) - (TAB_WIDTH / 2F)) + 10, 25, TAB_WIDTH - 20, 10, "X: ", "", -TileEntityMannequin.CONS_OFFSET_MAX, TileEntityMannequin.CONS_OFFSET_MAX, 0D, true, true, this);
        bipedOffsetYslider = new GuiCustomSlider(0, (int)((width / 2F) - (TAB_WIDTH / 2F)) + 10, 25 + 12, TAB_WIDTH - 20, 10, "Y: ", "", -TileEntityMannequin.CONS_OFFSET_MAX, TileEntityMannequin.CONS_OFFSET_MAX, 0D, true, true, this);
        bipedOffsetZslider = new GuiCustomSlider(0, (int)((width / 2F) - (TAB_WIDTH / 2F)) + 10, 25 + 24, TAB_WIDTH - 20, 10, "Z: ", "", -TileEntityMannequin.CONS_OFFSET_MAX, TileEntityMannequin.CONS_OFFSET_MAX, 0D, true, true, this);
        setSliderValue(bipedOffsetXslider, tileEntity.getOffsetX());
        setSliderValue(bipedOffsetYslider, tileEntity.getOffsetY());
        setSliderValue(bipedOffsetZslider, tileEntity.getOffsetZ());
        
        buttonList.add(resetOffsetButton);
        buttonList.add(bipedOffsetXslider);
        buttonList.add(bipedOffsetYslider);
        buttonList.add(bipedOffsetZslider);
        guiLoaded = true;
    }
    
    private void setSliderValue(GuiCustomSlider slider, double value) {
        slider.setValue(value);
        slider.precision = 2;
        slider.updateSlider();
    }

    @Override
    public void drawBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
        Rectangle_I_2D rec = new Rectangle_I_2D(0, 0, TAB_WIDTH, TAB_HEIGHT);
        rec.x = width / 2 - rec.width / 2;
        GuiUtils.drawContinuousTexturedBox(rec.x, rec.y, 0, 200, rec.width, rec.height, 38, 38, 4, zLevel);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == resetOffsetButton) {
            bipedOffsetXslider.setValue(0D);
            bipedOffsetYslider.setValue(0D);
            bipedOffsetZslider.setValue(0D);
            bipedOffsetXslider.updateSlider();
            bipedOffsetYslider.updateSlider();
            bipedOffsetZslider.updateSlider();
            sendData();
        }
    }

    @Override
    public void onChangeSliderValue(GuiSlider slider) {
        if (!guiLoaded) {
            return;
        }
        sendData();
    }
    
    public void sendData() {
        float offsetX = (float) bipedOffsetXslider.getValue();
        float offsetY = (float) bipedOffsetYslider.getValue();
        float offsetZ = (float) bipedOffsetZslider.getValue();
        
        boolean renderExtras = ((GuiMannequin)parent).tabExtraRenders.isExtraRenders.isChecked();
        boolean flying = ((GuiMannequin)parent).tabExtraRenders.isFlying.isChecked();
        boolean visible = ((GuiMannequin)parent).tabExtraRenders.isVisible.isChecked();
        TextureType textureType = TextureType.values()[((GuiMannequin)parent).tabTexture.textureTypeList.getListSelectedIndex()];
        String name = ((GuiMannequin)parent).tabTexture.nameTextbox.getText();
        int skinColour = ((GuiMannequin)parent).tabSkinAndHair.skinColour;
        int hairColour = ((GuiMannequin)parent).tabSkinAndHair.hairColour;
        MessageClientGuiMannequinData message = new MessageClientGuiMannequinData(offsetX, offsetY, offsetZ, skinColour,hairColour, name, renderExtras, flying, visible, textureType);
        PacketHandler.networkWrapper.sendToServer(message);
    }
}
