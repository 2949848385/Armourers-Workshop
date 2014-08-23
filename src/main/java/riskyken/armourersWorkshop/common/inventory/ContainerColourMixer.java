package riskyken.armourersWorkshop.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import riskyken.armourersWorkshop.common.items.ModItems;
import riskyken.armourersWorkshop.common.tileentities.TileEntityColourMixer;

public class ContainerColourMixer extends Container {

    private TileEntityColourMixer tileEntityColourMixer;

    public ContainerColourMixer(InventoryPlayer invPlayer,
            TileEntityColourMixer tileEntityColourMixer) {
        this.tileEntityColourMixer = tileEntityColourMixer;

        addSlotToContainer(new SlotPaintbrush(tileEntityColourMixer, 0, 144, 39));
        addSlotToContainer(new SlotOutput(tileEntityColourMixer, 1, 144, 76));

        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 189));
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 131 + y * 18));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        Slot slot = getSlot(slotID);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack result = stack.copy();

            if (slotID < 2) {
                if (!this.mergeItemStack(stack, 11, 38, false)) {
                    if (!this.mergeItemStack(stack, 2, 11, false)) {
                        return null;
                    }
                }
            } else {
                if (stack.getItem() == ModItems.paintbrush) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return null;
                    }
                } else {
                    return null;
                }
            }

            if (stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            slot.onPickupFromSlot(player, stack);

            return result;
        }

        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntityColourMixer.isUseableByPlayer(player);
    }

    public TileEntityColourMixer getTileEntity() {
        return tileEntityColourMixer;
    }
}
