package com.dot.tartarus.common.Caps.Gender;

import com.ibm.icu.util.GenderInfo;
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

public class IGenderProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<IGender> Gender = CapabilityManager.get(new CapabilityToken<>() {});

    private IGender gender = null;
    private final LazyOptional<IGender> optional = LazyOptional.of(this::createGender);

    private IGender createGender() {
        if (gender == null) gender = new IGender();
        return gender;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Gender) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createGender().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createGender().loadNBTData(nbt);
    }
}
