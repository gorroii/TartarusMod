package com.dot.tartarus.common.Items;

import com.dot.tartarus.TartarusMod;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TartarusMod.MOD_ID);

    public static final Rarity ARASAKA_RARITY = Rarity.create("arasaka", ChatFormatting.DARK_RED);
    public static final Rarity MILITARY_RARITY = Rarity.create("military", ChatFormatting.DARK_GREEN);






    public static final RegistryObject<Item> FINISHED_ARASAKA_MECHANISM = ITEMS.register("finished_arasaka_mechanism",
            () -> new Item(new Item.Properties().rarity(ARASAKA_RARITY)));
    public static final RegistryObject<Item> UNFINISHED_ARASAKA_MECHANISM = ITEMS.register("unfinished_arasaka_mechanism",
            () -> new Item(new Item.Properties().rarity(ARASAKA_RARITY)));

    public static final RegistryObject<Item> FINISHED_MILITARY_MECHANISM = ITEMS.register("finished_military_mechanism",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));
    public static final RegistryObject<Item> UNFINISHED_MILITARY_MECHANISM = ITEMS.register("unfinished_military_mechanism",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));

    public static final RegistryObject<Item> FINISHED_ALUMINIUM_MECHANISM = ITEMS.register("finished_aluminium_mechanism",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> UNFINISHED_ALUMINIUM_MECHANISM = ITEMS.register("unfinished_aluminium_mechanism",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> ARASAKA_MICROCHIP = ITEMS.register("arasaka_microchip",
            () -> new Item(new Item.Properties().rarity(ARASAKA_RARITY)));

    public static final RegistryObject<Item> TRIGGER_MECHANISM = ITEMS.register("trigger_mechanism",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));
    public static final RegistryObject<Item> ADVANCED_TRIGGER_MECHANISM = ITEMS.register("advanced_trigger_mechanism",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));

    public static final RegistryObject<Item> UPPER_RECIEVER_SG550 = ITEMS.register("upper_reciever-sg550",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));

    public static final RegistryObject<Item> BASIC_RIFLE_STOCK = ITEMS.register("basic_rifle_stock",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));
    public static final RegistryObject<Item> INCOMPLETE_SG550 = ITEMS.register("incomplete_sg550",
            () -> new Item(new Item.Properties().rarity(MILITARY_RARITY)));

    public static final RegistryObject<ClothItem> THONG = ITEMS.register("thong",
            () -> new ClothItem(new Item.Properties(),ClothesSlot.PANTIES));











    public static void register(IEventBus eventBus){ITEMS.register(eventBus);}
}
