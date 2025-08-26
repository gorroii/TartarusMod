package com.dot.tartarus.common.Caps.Eyes;

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

public class IEyeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<IEye> Eye = CapabilityManager.get(new CapabilityToken<>() {});

    private IEye eye = null;
    private final LazyOptional<IEye> optional = LazyOptional.of(this::createEye);

    private IEye createEye() {
        if (eye == null) eye = new IEye();
        return eye;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Eye) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createEye().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createEye().loadNBTData(nbt);
    }
}
