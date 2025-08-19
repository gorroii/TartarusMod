package com.dot.tartarus.common.UI;

import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class InventoryMenu extends AbstractContainerMenu {
    private final Player player;

    public InventoryMenu(int id, Inventory playerInventory) {
        super(ModMenus.INVENTORY_MENU.get(), id);
        this.player = playerInventory.player;

        // твой кап с одеждой
        player.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(cap -> {
            addSlot(new ClothSlot(cap.getInventory(), 0, 80, 8, player));
            addSlot(new ClothSlot(cap.getInventory(), 1, 80, 26, player));
            addSlot(new ClothSlot(cap.getInventory(), 2, 80, 44, player));
            addSlot(new ClothSlot(cap.getInventory(), 3, 80, 62, player));
            addSlot(new ClothSlot(cap.getInventory(), 4, 98, 26, player));
            addSlot(new ClothSlot(cap.getInventory(), 5, 116, 26, player));
            addSlot(new ClothSlot(cap.getInventory(), 6, 134, 26, player));
            addSlot(new ClothSlot(cap.getInventory(), 7, 152, 26, player));
            addSlot(new ClothSlot(cap.getInventory(), 8, 98, 44, player));
        });

        addPlayerSlots(new InvWrapper(playerInventory));
    }

    protected void addPlayerSlots(InvWrapper playerInventory) {
        int yStart = 30 + 18 * 3;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + yStart;
                this.addSlot(new SlotItemHandler(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = yStart + 58;
            this.addSlot(new SlotItemHandler(playerInventory, row, x, y));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        int otherSlots = this.slots.size() - 36;

        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            transferred = current.copy();

            if (index < otherSlots) {
                if (!this.moveItemStackTo(current, otherSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(current, 0, otherSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return transferred;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}