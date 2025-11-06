package com.dot.tartarus;

import com.dot.tartarus.Client.Keybinds;

import com.dot.tartarus.Network.Packets.OpenInventoryPacket;
import com.dot.tartarus.Network.Packets.SendStateChangePacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.common.Blocks.ModBlocks;
import com.dot.tartarus.common.Gson.ClothManager;
import com.dot.tartarus.common.Items.ModCreativeModTabs;
import com.dot.tartarus.common.Items.ModItems;


import com.dot.tartarus.common.Sounds.TRSounds;
import com.dot.tartarus.common.UI.InventoryScreen;
import com.dot.tartarus.common.UI.ModMenus;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(TartarusMod.MOD_ID)
public class TartarusMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "tartarus";



    private static final Logger LOGGER = LogUtils.getLogger();
    public TartarusMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Common setup
        modEventBus.addListener(this::commonSetup);

        // Client setup
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::registerKey);
            modEventBus.addListener(this::doClientStuff);
            MinecraftForge.EVENT_BUS.addListener(this::keyPressed);
        });

        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenus.CONTAINERS.register(modEventBus);
        TRSounds.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        TRNetwork.registerMessages();
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new ClothManager());
        });



    }
    @OnlyIn(Dist.CLIENT)
    public void registerKey(RegisterKeyMappingsEvent event) {
        Keybinds.registerKeys(event);
    }
    @OnlyIn(Dist.CLIENT)
    public void keyPressed(InputEvent.Key event) {
        if (Keybinds.OPEN_INVENTORY.isDown() && event.getAction() == GLFW.GLFW_PRESS) {
            TRNetwork.CHANNEL.sendToServer(new OpenInventoryPacket());
        }
        if (Keybinds.UNDRESS_TOP.isDown() && event.getAction() == GLFW.GLFW_PRESS){
            TRNetwork.CHANNEL.sendToServer(new SendStateChangePacket());








        }

    }

    @OnlyIn(Dist.CLIENT)
    public void doClientStuff(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenus.INVENTORY_MENU.get(), InventoryScreen::new);
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }





    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

    }
}

