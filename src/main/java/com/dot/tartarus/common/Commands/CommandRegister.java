package com.dot.tartarus.common.Commands;

import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandRegister {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        SetCharacterCommand.register(event.getDispatcher());
    }
    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event){

    }
}
