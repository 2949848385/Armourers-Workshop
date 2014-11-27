package riskyken.armourersWorkshop.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import riskyken.armourersWorkshop.client.gui.controls.GuiBookButton;
import riskyken.armourersWorkshop.client.gui.controls.GuiBookTextButton;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.utils.UtilColour;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGuideBook extends GuiScreen {
    
    private static final ResourceLocation bookTexture = new ResourceLocation(LibModInfo.ID.toLowerCase(), "textures/gui/guideBook.png");
    private static final ResourceLocation bookPageTexture = new ResourceLocation(LibModInfo.ID.toLowerCase(), "textures/gui/guideBookPage.png");
    
    /** Holds the number of pages in each chapter **/
    private final int[] chapters = {2, 2, 2, 2, 2, 4, 2};
    private final String bookName;
    
    private static int pageNumber = 1;
    
    private final int guiWidth;
    private final int guiHeight;
    private int guiLeft;
    private int guiTop;
    private ItemStack stack;
    
    private GuiBookButton backButton;
    private GuiBookButton forwardButton;
    
    @Override
    public void initGui() {
        super.initGui();
        guiLeft = width / 2 - guiWidth / 2;
        guiTop = height / 2 - guiHeight / 2;
        buttonList.clear();
        
        backButton = new GuiBookButton(0, this.guiLeft - 20, this.guiTop + 156, 3, 207, bookTexture);
        forwardButton = new GuiBookButton(1, this.guiLeft + 258, this.guiTop + 156, 3, 194, bookTexture);
        
        buttonList.add(backButton);
        buttonList.add(forwardButton);
        
        for (int i = 1; i < chapters.length; i++) {
            String chapterList =  getLocalizedText(".chapter" + (i + 1) + ".title");
            buttonList.add(new GuiBookTextButton(i + 2, this.guiLeft + 17, this.guiTop + 11 + 14 * i, fontRendererObj.getStringWidth(chapterList), chapterList));
        }
        lastRenderTick = System.currentTimeMillis();
    }
    
    public GuiGuideBook(ItemStack stack) {
        this.stack = stack;
        guiWidth = 256;
        guiHeight = 180;
        this.bookName = "guideBook";
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) { startPageTurnRight(); }
        if (button.id == 1) { startPageTurnLeft(); }
        
        if (button.id > 1) {
            setGoToChapter(button.id - 2);
        }
    }
    
    @Override
    protected void keyTyped(char key, int keyCode) {
        super.keyTyped(key, keyCode);
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
    }
    
    /** The amount the page has turned. 0.0 for none 1.0 for a full turn. */
    private float pageTurnAmount = 0F;
    PageState pageState = PageState.NONE;
    private float count;
    private long lastRenderTick;
    
    private void startPageTurnLeft() {
        pageState = PageState.TURN_LEFT;
        pageTurnAmount = 0F;
        nextPage();
    }
    
    private void startPageTurnRight() {
        pageState = PageState.TURN_RIGHT;
        pageTurnAmount = 0F;
        previousPage();
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float someFloat) {
        GL11.glColor4f(1, 1, 1, 1);
        long tickTime = System.currentTimeMillis() - lastRenderTick;
        lastRenderTick = System.currentTimeMillis();
        
        if (pageState != PageState.NONE) {
            float turnCenter = pageTurnAmount - 0.5F;
            if (turnCenter < 0) { turnCenter = -turnCenter; }
            turnCenter = -turnCenter + 0.5F;
            pageTurnAmount += ((0.006F) + (turnCenter * 0.08F) * (tickTime * 0.1F));
        }
        
        if (pageTurnAmount > 1F) {
            pageTurnAmount = 0F;
            pageState = PageState.NONE;
            count = 0;
        }
        
        if (pageState != PageState.NONE) {
            count = (pageState.movement * pageTurnAmount) * 180;
        }
        
        if (count > 90F) {
            count -= 180F;
        }
        if (count < -90F) {
            count += 180F;
        }
        
        mc.renderEngine.bindTexture(bookTexture);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth, this.guiHeight);
        
        for (int k = 0; k < this.buttonList.size(); ++k) {
            if (this.buttonList.get(k) instanceof GuiBookTextButton) {
                ((GuiButton)this.buttonList.get(k)).visible = pageNumber == 1;
                if (pageNumber == 3 & pageState == PageState.TURN_LEFT) {
                    ((GuiButton)this.buttonList.get(k)).visible = true;
                }
            }
        }
        backButton.visible = !isFirstPage(pageNumber);
        forwardButton.visible = !isLastPage(pageNumber);
        
        for (int k = 0; k < this.buttonList.size(); ++k) {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc, mouseX, mouseY);
        }
        
        if (pageState == PageState.NONE) {
            renderPageText(pageNumber, 17, 1);
            renderPageText(pageNumber + 1, 134, 2);
        }
        if (pageState == PageState.TURN_LEFT) {
            renderPageText(pageNumber - 2, 17, 1);
            renderPageText(pageNumber + 1, 134, 2);
        }
        if (pageState == PageState.TURN_RIGHT) {
            renderPageText(pageNumber, 17, 1);
            renderPageText(pageNumber + 3, 134, 2);
        }
        
        if (pageState != PageState.NONE) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glTranslatef(this.guiLeft + 128, 0, 0);
            GL11.glRotatef(count, 0, 1, 0);
            GL11.glTranslatef(-(this.guiLeft + 128), 0, 0);
            GL11.glColor4f(1, 1, 1, 1);
            if (count >= 0) {
                mc.renderEngine.bindTexture(bookPageTexture);
                drawTexturedModalRect(this.guiLeft + 10, this.guiTop + 7, 10, 7, 118, 165);
                if (pageState == PageState.TURN_LEFT) {
                    renderPageText(pageNumber, 17, 1);
                }
                if (pageState == PageState.TURN_RIGHT) {
                    renderPageText(pageNumber + 2, 17, 1);
                }
            } else {
                GL11.glTranslatef(118, 0, 0);
                mc.renderEngine.bindTexture(bookPageTexture);
                drawTexturedModalRect(this.guiLeft + 10, this.guiTop + 7, 128, 7, 118, 165);
                GL11.glTranslatef(-118, 0, 0);
                if (pageState == PageState.TURN_LEFT) {
                    renderPageText(pageNumber - 1, 134, 2);
                }
                if (pageState == PageState.TURN_RIGHT) {
                    renderPageText(pageNumber + 1, 134, 2);
                }
            }
            
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }
    
    private void renderPageText(int page, int left, int side) {
        String pageText =  getLocalizedText(".chapter" + (getChapterFromPageNumber(page) + 1) + ".page" + getChapterPageFromPageNumber(page) + ".text");
        String chapterTitle =  getLocalizedText(".chapter" + (getChapterFromPageNumber(page) + 1) + ".title");
        int pageCenter = 69;
        if (side == 2) {
            pageCenter = 186;
        }
        
        if (pageText != null) {
            fontRendererObj.drawSplitString(pageText, this.guiLeft + left, this.guiTop + 25, 104, UtilColour.getMinecraftColor(8));
        }
        if (chapterTitle != null) {
            renderStringCenter(chapterTitle, this.guiLeft + left, this.guiTop + 12);
        }
        
        renderStringCenter( page + " - " + getTotalPages(), this.guiLeft + left, this.guiTop + 160);
    }
    
    private void renderStringCenter(String text, int x, int y) {
        int xCenter = 104 / 2 - fontRendererObj.getStringWidth(text) / 2;
        fontRendererObj.drawString(text, x + xCenter, y, UtilColour.getMinecraftColor(7));
    }
    
    private String getLocalizedText(String item) {
        String unlocalized =  "book." + LibModInfo.ID.toLowerCase() + ":" + bookName + item;
        String localized = StatCollector.translateToLocal(unlocalized);
        if (!unlocalized.equals(localized)) {
            localized = localized.replaceAll("&n", "\n");
            localized = localized.replaceAll("&p", "\n\n");
            return localized;
        }
        return null;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private boolean isFirstPage(int pageNumber) {
        return pageNumber == 1;
    }
    
    private boolean isLastPage(int pageNumber) {
        return pageNumber >= getTotalPages() - 1;
    }
    
    private void nextPage() {
        this.pageNumber += 2;
        if (pageNumber >= getTotalPages() + 1) { pageNumber = getTotalPages() - 1; }
    }
    
    private void previousPage() {
        this.pageNumber -= 2;
        if (pageNumber < 1) { pageNumber = 1; }
    }
    
    private void setGoToChapter(int chapterNumber) {
        int pageCount = 0;
        for (int i = 0; i < chapters.length; i++) {
            if (chapterNumber == i) {
                this.pageNumber = pageCount + 1;
            }
            pageCount += getPagesInChapter(i);
        }
    }
    
    private void goToPage(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    private int getChapterPageFromPageNumber(int pageNumber) {
        int pageCount = 0;
        for (int i = 0; i < getChapterFromPageNumber(pageNumber); i++) {
            if ((chapters[i] & 1) == 0 ) {
                pageCount += chapters[i];
            } else {
                pageCount += chapters[i] + 1;
            }
        }
        return pageNumber - pageCount;
    }
    
    private int getChapterFromPageNumber(int pageNumber) {
        int pageCount = 0;
        for (int i = 0; i < chapters.length; i++) {
            pageCount += getPagesInChapter(i);
            if (pageCount >= pageNumber) {
                return i;
            }
        }
        return 0;
    }
    
    private int getPagesInChapter(int chapterNumber) {
        if ((chapters[chapterNumber] & 1) == 0 ) {
            return chapters[chapterNumber];
        } else {
            return chapters[chapterNumber] + 1;
        }
    }
    
    private int getTotalPages() {
        int pageCount = 0;
        for (int i = 0; i < chapters.length; i++) {
            pageCount += getPagesInChapter(i);
        }
        return pageCount;
    }
    
    private enum PageState {
        NONE(0),
        TURN_LEFT(-1),
        TURN_RIGHT(1);
        
        public final int movement;
        
        private PageState(int movement) {
            this.movement = movement;
        }
    }
}
