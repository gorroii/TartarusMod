package com.dot.tartarus.common.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltipComponents, flag);

        // Add slot information with translation support
        tooltipComponents.add(
                Component.translatable("tooltip.clothitem.slot").withStyle(ChatFormatting.GRAY)
                        .append(
                                Component.translatable("slot.cloth." + slot.name().toLowerCase())
                                        .withStyle(ChatFormatting.YELLOW)));
        // Example: Add durability if the item has damage

    }
}
