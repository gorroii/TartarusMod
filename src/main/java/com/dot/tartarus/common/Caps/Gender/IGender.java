package com.dot.tartarus.common.Caps.Gender;

import net.minecraft.nbt.CompoundTag;

public class IGender {
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void copyFrom(IGender source) {
        this.gender = source.gender;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("gender", gender);
    }

    public void loadNBTData(CompoundTag nbt) {
        gender = nbt.getInt("gender");
    }
}