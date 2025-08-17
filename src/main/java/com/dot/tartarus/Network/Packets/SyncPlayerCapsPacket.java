package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncPlayerCapsPacket {
    private final UUID playerUUID;
    private final int gender;
    private final int skin;
    private final int hair;

    public SyncPlayerCapsPacket(UUID playerUUID, int gender, int skin, int hair) {
        this.playerUUID = playerUUID;
        this.gender = gender;
        this.skin = skin;
        this.hair = hair;
    }


    public static void encode(SyncPlayerCapsPacket pkt, FriendlyByteBuf buf) {
        buf.writeUUID(pkt.playerUUID);
        buf.writeInt(pkt.gender);
        buf.writeInt(pkt.skin);
        buf.writeInt(pkt.hair);
    }


    public static SyncPlayerCapsPacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerCapsPacket(buf.readUUID(), buf.readInt(), buf.readInt(), buf.readInt());
    }


    public static void handle(SyncPlayerCapsPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.level.getPlayerByUUID(pkt.playerUUID);
            if (player != null) {
                player.getCapability(IGenderProvider.Gender).ifPresent(cap -> cap.setGender(pkt.gender));
                player.getCapability(ISkinProvider.Skin).ifPresent(cap -> cap.setSkin(pkt.skin));
                player.getCapability(IHairProvider.Hair).ifPresent(cap -> cap.setHair(pkt.hair));
                SkinUtil.ClearCache(player); // force cache refresh
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
