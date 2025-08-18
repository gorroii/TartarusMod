package com.dot.tartarus.common.Events;

import com.dot.tartarus.Network.Packets.GenderPacket;
import com.dot.tartarus.Network.Packets.HairPacket;
import com.dot.tartarus.Network.Packets.SkinPacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Edited.IEditedProvider;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Timer;

import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendAllCapabilitiesTo;
import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendCapabilitiesToAll;

@Mod.EventBusSubscriber(modid = TartarusMod.MOD_ID)
public class TREvent {

    // Attach capabilities to players
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {

                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "gender"), new IGenderProvider());


                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "skin"), new ISkinProvider());

                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "hair"), new IHairProvider());

                event.addCapability(new ResourceLocation(TartarusMod.MOD_ID, "edited"), new IEditedProvider());


        }
    }

    // Copy capabilities on death/respawn and sync to client
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {

            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();
            event.getOriginal().reviveCaps();
            int oldgender = oldPlayer.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);
            int newgender = newPlayer.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);



            oldPlayer.getCapability(IGenderProvider.Gender).ifPresent(oldCap ->
                    newPlayer.getCapability(IGenderProvider.Gender).ifPresent(newCap ->
                            newCap.copyFrom(oldCap)
                    )
            );

            oldPlayer.getCapability(ISkinProvider.Skin).ifPresent(oldCap ->
                    newPlayer.getCapability(ISkinProvider.Skin).ifPresent(newCap ->
                            newCap.copyFrom(oldCap)
                    )
            );

            oldPlayer.getCapability(IHairProvider.Hair).ifPresent(oldCap ->
                    newPlayer.getCapability(IHairProvider.Hair).ifPresent(newCap ->
                            newCap.copyFrom(oldCap)
                    )
            );

            oldPlayer.getCapability(IEditedProvider.Edited).ifPresent(oldCap ->
                    newPlayer.getCapability(IEditedProvider.Edited).ifPresent(newCap ->
                            newCap.copyFrom(oldCap)
                    )

            );




                event.getOriginal().invalidateCaps();






        }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {


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