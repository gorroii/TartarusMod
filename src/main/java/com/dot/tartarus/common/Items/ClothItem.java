package com.dot.tartarus.common.Items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ClothItem extends Item implements Cloth {
    private final ClothesSlot slot;
    public ClothItem(Properties pProperties, ClothesSlot slot) {
        super(new Properties().stacksTo(1));
        this.slot = slot;
    }


    @Override
    public ClothesSlot getSlot() {
        return slot;
    }
    protected ResourceLocation getTexture() {
        return new ResourceLocation(ForgeRegistries.ITEMS.getKey(this).getNamespace(), "textures/entity/clothes/" + ForgeRegistries.ITEMS.getKey(this).getPath() + ".png");
    }
}
