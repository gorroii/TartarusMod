package com.dot.tartarus.common.Caps.Eyes;

import net.minecraft.nbt.CompoundTag;

public class IEye {
    private int eye;

    public int getEye() {
        return eye;
    }

    public void setEye(int eye) {
        this.eye = eye;
    }

    public void copyFrom(IEye source) {
        this.eye = source.eye;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("eye", eye);
    }

    public void loadNBTData(CompoundTag nbt) {
        eye = nbt.getInt("eye");
    }
}