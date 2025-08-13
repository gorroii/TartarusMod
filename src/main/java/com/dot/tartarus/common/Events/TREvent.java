package com.dot.tartarus.common.Events;

import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TartarusMod.MOD_ID)
public class TREvent {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(IGenderProvider.Gender).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "gender"), new IGenderProvider());
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(IGenderProvider.Gender).ifPresent(oldStore -> {
                event.getOriginal().getCapability(IGenderProvider.Gender).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(IGenderProvider.Gender).ifPresent(genderCap -> {
                genderCap.setGender(1);
            });
        }
    }

}
