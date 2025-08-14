package com.dot.tartarus.common.Events;

import com.dot.tartarus.Network.Packets.GenderPacket;
import com.dot.tartarus.Network.Packets.HairPacket;
import com.dot.tartarus.Network.Packets.SkinPacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHair;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = TartarusMod.MOD_ID)
public class TREvent {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(IGenderProvider.Gender).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "gender"), new IGenderProvider());
            }
            if(!event.getObject().getCapability(ISkinProvider.Skin).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "skin"), new ISkinProvider());
            }
            if(!event.getObject().getCapability(IHairProvider.Hair).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "hair"), new IHairProvider());
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
            event.getOriginal().getCapability(ISkinProvider.Skin).ifPresent(oldStore -> {
                event.getOriginal().getCapability(ISkinProvider.Skin).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(IHairProvider.Hair).ifPresent(oldStore -> {
                event.getOriginal().getCapability(IHairProvider.Hair).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(IGenderProvider.Gender).ifPresent(genderCap -> genderCap.setGender(1));

            TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> {
                return (ServerPlayer)event.player;
            }), new GenderPacket(1));

            event.player.getCapability(ISkinProvider.Skin).ifPresent(skinCap -> skinCap.setSkin(2));

            TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> {
                return (ServerPlayer)event.player;
            }), new SkinPacket(2));

            event.player.getCapability(IHairProvider.Hair).ifPresent(hairCap -> hairCap.setHair(2));

            TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> {
                return (ServerPlayer)event.player;
            }), new HairPacket(2));
        }
    }

}
