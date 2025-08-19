package com.dot.tartarus.common.Items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface Cloth {
    ClothesSlot getSlot();

    default boolean canDropOnDeath(Player player, ItemStack stack) {
        return true;
    }
}
