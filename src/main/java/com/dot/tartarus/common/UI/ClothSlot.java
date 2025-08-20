package com.dot.tartarus.common.UI;

import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Items.Cloth;
import com.dot.tartarus.common.Items.ClothesSlot;
import com.dot.tartarus.common.Utils.PacketSyncUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ClothSlot extends SlotItemHandler {

    private final Player player;
    private final ClothesSlot slot;

    public ClothSlot(IItemHandler pContainer, int pIndex, int pX, int pY, Player player) {
        super(pContainer, pIndex, pX, pY);
        this.player = player;
        this.slot = ClothesSlot.getFromID(pIndex);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.getItem() instanceof Cloth && ((Cloth) pStack.getItem()).getSlot() == slot;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (!player.level().isClientSide) {
            PacketSyncUtils.sendClothesToAll(player);
            PacketSyncUtils.sendAllClothesTo(player);
            }
        }
    }

