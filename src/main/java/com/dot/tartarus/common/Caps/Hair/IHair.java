package com.dot.tartarus.common.Caps.Hair;

import net.minecraft.nbt.CompoundTag;

public class IHair {
    private int Hair;

    public int getHair(){
        return Hair;
    }
    public void setHair(int set){
        this.Hair = set;

    }

    public void copyFrom(IHair source){
        this.Hair = source.Hair;
    }
    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("hair", Hair);
    }
    public void loadNBTData(CompoundTag nbt){
        Hair = nbt.getInt("hair");
    }
}