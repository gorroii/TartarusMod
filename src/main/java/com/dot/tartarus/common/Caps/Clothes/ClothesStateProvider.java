package com.dot.tartarus.common.Caps.Clothes;

import com.dot.tartarus.common.Caps.Hair.IHair;
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

public class ClothesStateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ClothesStateCap> State = CapabilityManager.get(new CapabilityToken<>() {
    });



    private ClothesStateCap state = null;
    private final LazyOptional<ClothesStateCap> optional = LazyOptional.of(this::createState);

    private ClothesStateCap createState(){
        if(this.state == null) {
            this.state = new ClothesStateCap();
        }
        return this.state;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == State) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createState().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createState().loadNBTData(nbt);
    }
}