package com.dot.tartarus.common.Caps.Skin;

import com.dot.tartarus.common.Caps.Skin.ISkin;
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

public class ISkinProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ISkin> Skin = CapabilityManager.get(new CapabilityToken<>() {});

    private ISkin skin = null;
    private final LazyOptional<ISkin> optional = LazyOptional.of(this::createSkin);

    private ISkin createSkin() {
        if (skin == null) skin = new ISkin();
        return skin;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Skin) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSkin().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSkin().loadNBTData(nbt);
    }
}

