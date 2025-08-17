package com.dot.tartarus.common.Caps.Edited;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class IEditedProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<IEdited> Edited = CapabilityManager.get(new CapabilityToken<>() {});

    private IEdited edited = null;
    private final LazyOptional<IEdited> optional = LazyOptional.of(this::createEdited);

    private IEdited createEdited() {
        if (edited == null) edited = new IEdited();
        return edited;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Edited) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createEdited().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createEdited().loadNBTData(nbt);
    }
}
