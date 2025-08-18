package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendAllCapabilitiesTo;
import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendCapabilitiesToAll;

public class SkinPacket {
    private final float skin;

    public SkinPacket(float skin) {
        this.skin = skin;
    }

    public static void encode(SkinPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.skin);

    }

    public static SkinPacket decode(FriendlyByteBuf buf) {
        return new SkinPacket(buf.readFloat());
    }

    public static void handle(SkinPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ((Player)player).getCapability(ISkinProvider.Skin).ifPresent((skinCap) -> {
                    skinCap.setSkin((int)msg.skin);
                    sendAllCapabilitiesTo(player);
                    sendCapabilitiesToAll(player);
                    SkinUtil.ClearCache(player);
                });
            }

        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}
