package com.dot.tartarus.common.Items;


import com.dot.tartarus.TartarusMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TartarusMod.MOD_ID);

    public static void register(IEventBus eventBus) {

        final RegistryObject<CreativeModeTab> TARTARUS_TAB = CREATIVE_MODE_TABS.register("tartarus",
                () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.FINISHED_ARASAKA_MECHANISM.get()))
                        .title(Component.translatable("creativetab.tartarus"))
                        .displayItems((itemDisplayParameters, output) -> {
                            output.accept(ModItems.FINISHED_ARASAKA_MECHANISM.get());
                            output.accept(ModItems.FINISHED_MILITARY_MECHANISM.get());
                            output.accept(ModItems.FINISHED_ALUMINIUM_MECHANISM.get());


                        })
                        .build());
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
