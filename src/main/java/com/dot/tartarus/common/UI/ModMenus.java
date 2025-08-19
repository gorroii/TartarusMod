package com.dot.tartarus.common.UI;

import com.dot.tartarus.TartarusMod;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TartarusMod.MOD_ID);

    public static final RegistryObject<MenuType<InventoryMenu>> INVENTORY_MENU =
            CONTAINERS.register("inventory_menu",
                    () -> IForgeMenuType.create((windowId, inv, buf) -> new InventoryMenu(windowId, inv)));

}