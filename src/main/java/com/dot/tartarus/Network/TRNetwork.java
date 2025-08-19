package com.dot.tartarus.Network;

import com.dot.tartarus.Network.Packets.*;
import com.dot.tartarus.TartarusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.IndexedMessageCodec;
import net.minecraftforge.network.simple.SimpleChannel;

public class TRNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TartarusMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int nextId() {
        return packetId++;
    }
    public static void registerMessages() {
        CHANNEL.registerMessage(nextId(), GenderPacket.class, GenderPacket::encode, GenderPacket::decode,GenderPacket::handle);
        CHANNEL.registerMessage(nextId(), SkinPacket.class, SkinPacket::encode, SkinPacket::decode, SkinPacket::handle);
        CHANNEL.registerMessage(nextId(), HairPacket.class, HairPacket::encode, HairPacket::decode, HairPacket::handle);
        CHANNEL.registerMessage(nextId(), SyncPlayerCapsPacket.class, SyncPlayerCapsPacket::encode, SyncPlayerCapsPacket::decode, SyncPlayerCapsPacket::handle);
        CHANNEL.registerMessage(nextId(), SyncClothesCap.class, SyncClothesCap::encode, SyncClothesCap::decode, SyncClothesCap::handle);
        CHANNEL.registerMessage(nextId(), OpenInventoryPacket.class,
                OpenInventoryPacket::encode,
                OpenInventoryPacket::decode,
                OpenInventoryPacket::handle);

    }
}
