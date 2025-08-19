package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.common.UI.InventoryMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenInventoryPacket {

    public OpenInventoryPacket() {}

    public static void encode(OpenInventoryPacket msg, FriendlyByteBuf buf) {
        // пусто
    }

    public static OpenInventoryPacket decode(FriendlyByteBuf buf) {
        return new OpenInventoryPacket();
    }

    public static void handle(OpenInventoryPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new InventoryMenu(id, inv),
                        Component.literal("Clothes Inventory")));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
