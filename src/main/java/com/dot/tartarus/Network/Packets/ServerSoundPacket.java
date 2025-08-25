package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Events.TREvent;
import com.dot.tartarus.common.Sounds.TRSounds;
import com.dot.tartarus.common.UI.InventoryMenu;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerSoundPacket {
    private final SoundEvent sound;
    public ServerSoundPacket(SoundEvent sound) {
        this.sound = sound;
    }

    public static void encode(ServerSoundPacket msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.sound.getLocation());
    }

    public static ServerSoundPacket decode(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(id); // in 1.20.1
        return new ServerSoundPacket(sound);
    }

    public static void handle(ServerSoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            // Get the player's eye position
            Vec3 eyePos = player.getEyePosition();

// Get the player's look vector (normalized direction the player is facing)
            Vec3 lookVec = player.getLookAngle();

// Offset forward by 0.1 blocks
            Vec3 soundPos = eyePos.add(lookVec.scale(0.1));

            player.level().playSound(null, soundPos.x,soundPos.y, soundPos.z,msg.sound, SoundSource.PLAYERS, 0.5f, 1.0f);

        });
        ctx.get().setPacketHandled(true);
    }
}
