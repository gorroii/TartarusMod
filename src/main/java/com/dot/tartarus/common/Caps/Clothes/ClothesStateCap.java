package com.dot.tartarus.common.Caps.Clothes;


import net.minecraft.nbt.CompoundTag;

public class ClothesStateCap {
    private int State;

    public int getState(){
        return State;
    }
    public void setState(int set){
        this.State = set;

    }


    public void copyFrom(ClothesStateCap source){
        this.State = source.State;
    }
    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("state", State);
    }
    public void loadNBTData(CompoundTag nbt){
        State = nbt.getInt("state");
    }
}