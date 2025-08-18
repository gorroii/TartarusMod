package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendAllCapabilitiesTo;
import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendCapabilitiesToAll;

public class HairPacket {
    private final float hair;

    public HairPacket(float skin) {
        this.hair = skin;
    }

    public static void encode(HairPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.hair);

    }

    public static HairPacket decode(FriendlyByteBuf buf) {
        return new HairPacket(buf.readFloat());
    }

    public static void handle(HairPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ((Player)player).getCapability(IHairProvider.Hair).ifPresent((skinCap) -> {
                    skinCap.setHair((int)msg.hair);
                    sendAllCapabilitiesTo(player);
                    sendCapabilitiesToAll(player);
                    SkinUtil.ClearCache(player);

                });
            }

        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}