package com.dot.tartarus.common.Events;

import com.dot.tartarus.Network.Packets.GenderPacket;
import com.dot.tartarus.Network.Packets.HairPacket;
import com.dot.tartarus.Network.Packets.SkinPacket;
import com.dot.tartarus.Network.Packets.SyncPlayerCapsPacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHair;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Commands.SetCharacterCommand;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static com.dot.tartarus.common.Utils.PacketSyncUtils.sendAllCapabilitiesTo;
import static com.dot.tartarus.common.Utils.PacketSyncUtils.sendCapabilitiesToAll;

@Mod.EventBusSubscriber(modid = TartarusMod.MOD_ID)
public class TREvent {

    // Attach capabilities to players
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(IGenderProvider.Gender).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "gender"), new IGenderProvider());
            }
            if (!player.getCapability(ISkinProvider.Skin).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "skin"), new ISkinProvider());
            }
            if (!player.getCapability(IHairProvider.Hair).isPresent()) {
                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "hair"), new IHairProvider());
            }
        }
    }

    // Copy capabilities on death/respawn and sync to client
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player oldPlayer = event.getOriginal();
            ServerPlayer newPlayer = (ServerPlayer) event.getOriginal();

            SkinUtil.ClearCache(oldPlayer);

            // Copy gender
            oldPlayer.getCapability(IGenderProvider.Gender).ifPresent(oldCap ->
                    newPlayer.getCapability(IGenderProvider.Gender).ifPresent(newCap -> {
                        newCap.copyFrom(oldCap);
                        TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> newPlayer),
                                new GenderPacket(newCap.getGender()));
                    })
            );

            // Copy skin
            oldPlayer.getCapability(ISkinProvider.Skin).ifPresent(oldCap ->
                    newPlayer.getCapability(ISkinProvider.Skin).ifPresent(newCap -> {
                        newCap.copyFrom(oldCap);
                        TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> newPlayer),
                                new SkinPacket(newCap.getSkin()));
                    })
            );

            // Copy hair
            oldPlayer.getCapability(IHairProvider.Hair).ifPresent(oldCap ->
                    newPlayer.getCapability(IHairProvider.Hair).ifPresent(newCap -> {
                        newCap.copyFrom(oldCap);
                        TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> newPlayer),
                                new HairPacket(newCap.getHair()));


                    })

            );
            sendCapabilitiesToAll(newPlayer);
        }
    }

    // Clear cached skins on login
    @SubscribeEvent
    public static void onLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        SkinUtil.ClearCache(event.getEntity());

        // Also send full capability sync to client on login
        Player player = event.getEntity();
        player.getCapability(IGenderProvider.Gender).ifPresent(cap ->
                TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new GenderPacket(cap.getGender()))
        );
        player.getCapability(ISkinProvider.Skin).ifPresent(cap ->
                TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new SkinPacket(cap.getSkin()))
        );
        player.getCapability(IHairProvider.Hair).ifPresent(cap ->
                TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new HairPacket(cap.getHair()))
        );

        sendCapabilitiesToAll(player); // send joined’s data to everyone
        sendAllCapabilitiesTo(player);
    }
}