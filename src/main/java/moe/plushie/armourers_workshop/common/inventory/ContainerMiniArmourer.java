package moe.plushie.armourers_workshop.common.inventory;

import moe.plushie.armourers_workshop.common.inventory.slot.SlotOutput;
import moe.plushie.armourers_workshop.common.inventory.slot.SlotSkinTemplate;
import moe.plushie.armourers_workshop.common.tileentities.TileEntityMiniArmourer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMiniArmourer extends Container {

    private TileEntityMiniArmourer tileEntity;
    
    public ContainerMiniArmourer(InventoryPlayer invPlayer, TileEntityMiniArmourer tileEntity) {
        this.tileEntity = tileEntity;
        
        addSlotToContainer(new SlotSkinTemplate(tileEntity, 0, 37, 58));
        addSlotToContainer(new SlotOutput(tileEntity, 1, 119, 58));
        
        int hotBarY = 152;
        int playerInvY = 94;
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, hotBarY));
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, playerInvY + y * 18));
            }
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotID) {
        return null;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUsableByPlayer(entityPlayer);
    }
    
    public TileEntityMiniArmourer getTileEntity() {
        return tileEntity;
    }
}
