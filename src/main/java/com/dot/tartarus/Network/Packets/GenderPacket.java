package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GenderPacket {
    private final float gender;

    public GenderPacket(float gender) {
        this.gender = gender;
    }

    public static void encode(GenderPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.gender);

    }

    public static GenderPacket decode(FriendlyByteBuf buf) {
        return new GenderPacket(buf.readFloat());
    }

    public static void handle(GenderPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ((Player)player).getCapability(IGenderProvider.Gender).ifPresent((genderCap) -> {
                    genderCap.setGender((int)msg.gender);
                });
            }

        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}

