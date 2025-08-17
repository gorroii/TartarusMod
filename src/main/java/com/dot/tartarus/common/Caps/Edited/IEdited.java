package com.dot.tartarus.common.Caps.Edited;

import net.minecraft.nbt.CompoundTag;

public class IEdited {
    private int edited;

    public int getEdited() {
        return edited;
    }

    public void setGender(int gender) {
        this.edited = gender;
    }

    public void copyFrom(IEdited source) {
        this.edited = source.edited;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("edited", edited);
    }

    public void loadNBTData(CompoundTag nbt) {
        edited = nbt.getInt("edited");
    }
}