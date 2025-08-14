package com.dot.tartarus.common.Caps.Skin;


import net.minecraft.nbt.CompoundTag;

public class ISkin {
    private int Skin;

    public int getSkin(){
        return Skin;
    }
    public void setSkin(int set){
        this.Skin = set;

    }

    public void copyFrom(ISkin source){
        this.Skin = source.Skin;
    }
    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("skin", Skin);
    }
    public void loadNBTData(CompoundTag nbt){
        Skin = nbt.getInt("skin");
    }
}
