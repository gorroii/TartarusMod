package com.dot.tartarus.common.Caps.Hair;

import com.dot.tartarus.common.Caps.Gender.IGender;
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

public class IHairProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<IHair> Hair = CapabilityManager.get(new CapabilityToken<IHair>() {});

    private IHair hair = null;
    private final LazyOptional<IHair> optional = LazyOptional.of(this::createHair);

    private IHair createHair(){
        if(this.hair == null) {
            this.hair = new IHair();
        }
        return this.hair;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == Hair) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createHair().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createHair().loadNBTData(nbt);
    }
}