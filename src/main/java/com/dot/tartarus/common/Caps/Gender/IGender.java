package com.dot.tartarus.common.Caps.Gender;

import net.minecraft.nbt.CompoundTag;

public class IGender {
    private int Gender;

    public int getGender(){
        return Gender;
    }
    public void setGender(int set){
        this.Gender = set;

    }

    public void copyFrom(IGender source){
        this.Gender = source.Gender;
    }
    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("gender", Gender);
    }
    public void loadNBTData(CompoundTag nbt){
        Gender = nbt.getInt("gender");
    }
}