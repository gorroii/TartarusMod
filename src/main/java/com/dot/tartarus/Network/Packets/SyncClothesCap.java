package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Utils.PacketSyncUtils;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncClothesCap {
    private final CompoundTag data; // Just store the capability's NBT directly
    private final int entityId;

    public SyncClothesCap(CompoundTag data, int entityId) {
        this.data = data;
        this.entityId = entityId;
    }

    public static void encode(SyncClothesCap msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data); // Write the raw capability NBT (usually a ListTag or CompoundTag with "Items")
        buf.writeInt(msg.entityId);
    }

    public static SyncClothesCap decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        int entityId = buf.readInt();
        return new SyncClothesCap(tag, entityId);
    }

    public static void handle(SyncClothesCap msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.level.getEntity(msg.entityId);
            if (entity instanceof Player player) {
                player.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(cap -> {
                    cap.readNBT(msg.data);
                });

                SkinUtil.ClearCache(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}