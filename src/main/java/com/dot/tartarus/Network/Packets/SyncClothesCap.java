package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncClothesCap {
    private final CompoundTag invTag;
    private final int entityId;

    public SyncClothesCap(CompoundTag invTag, int entityId) {
        this.invTag = invTag;
        this.entityId = entityId;
    }

    public static void encode(SyncClothesCap msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.invTag);
        buf.writeInt(msg.entityId);
    }

    public static SyncClothesCap decode(FriendlyByteBuf buf) {
        return new SyncClothesCap(buf.readNbt(), buf.readInt());
    }

    public static void handle(SyncClothesCap msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(cap -> {
                    if (msg.invTag != null && msg.invTag.contains("inv")) {
                        cap.readNBT(msg.invTag.get("inv"));
                        SkinUtil.ClearCache((Player) entity);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}