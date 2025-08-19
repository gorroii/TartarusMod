package com.dot.tartarus.common.Caps.Clothes;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

public class ClothesProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    public static final Capability<ClothesCaps> CLOTHES_INVENTORY = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<ClothesCaps> inventory;

    public ClothesProvider(Player player) {
        this.inventory = LazyOptional.of(() -> new ClothesCaps(player));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CLOTHES_INVENTORY) {
            return inventory.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return inventory.orElseThrow(() -> new IllegalArgumentException("Inventory must not be empty")).writeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        inventory.orElseThrow(() -> new IllegalArgumentException("Inventory must not be empty!")).readNBT(nbt);
    }
}
