package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClearCachePacket {
    private final UUID playerUUID;

    public ClearCachePacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public static void encode(ClearCachePacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
    }

    public static ClearCachePacket decode(FriendlyByteBuf buf) {
        return new ClearCachePacket(buf.readUUID());
    }

    public static void handle(ClearCachePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.level.getPlayerByUUID(msg.playerUUID);
            if (player != null) {
                SkinUtil.ClearCache(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
