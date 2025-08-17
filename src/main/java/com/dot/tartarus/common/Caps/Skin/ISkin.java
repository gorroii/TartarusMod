package com.dot.tartarus.common.Caps.Skin;


import net.minecraft.nbt.CompoundTag;

public class ISkin {
    private int skin;

    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public void copyFrom(ISkin source) {
        this.skin = source.skin;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("skin", skin);
    }

    public void loadNBTData(CompoundTag nbt) {
        skin = nbt.getInt("skin");
    }
}
