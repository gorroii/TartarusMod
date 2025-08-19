package com.dot.tartarus.common.Utils;

import com.dot.tartarus.Network.Packets.SyncClothesCap;
import com.dot.tartarus.Network.Packets.SyncPlayerCapsPacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.common.Caps.Clothes.ClothesCaps;
import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class PacketSyncUtils {
    public static void sendCapabilitiesToAll(Player player) {
        int gender = player.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);
        int skin = player.getCapability(ISkinProvider.Skin).map(skincap -> skincap.getSkin()).orElse(null);
        int hair = player.getCapability(IHairProvider.Hair).map(haircap -> haircap.getHair()).orElse(null);

        TRNetwork.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new SyncPlayerCapsPacket(player.getUUID(), gender, skin, hair)
        );
    }

    public static void sendAllCapabilitiesTo(Player target) {
        ServerLevel level = (ServerLevel) target.level();
        for (ServerPlayer other : level.players()) {
            int gender = other.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);
            int skin = other.getCapability(ISkinProvider.Skin).map(skincap -> skincap.getSkin()).orElse(null);
            int hair = other.getCapability(IHairProvider.Hair).map(haircap -> haircap.getHair()).orElse(null);

            TRNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) target),
                    new SyncPlayerCapsPacket(other.getUUID(), gender, skin, hair)
            );
        }
    }
    public static void sendClothesToAll(Player player) {
        CompoundTag clothesTag = player.getCapability(ClothesProvider.CLOTHES_INVENTORY)
                .map(ClothesCaps::writeNBT)
                .orElse(new CompoundTag());

        TRNetwork.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new SyncClothesCap(clothesTag, player.getId())
        );


    }

    public static void sendAllClothesTo(Player target) {
        ServerLevel level = (ServerLevel) target.level();
        for (ServerPlayer other : level.players()) {
            CompoundTag clothesTag = other.getCapability(ClothesProvider.CLOTHES_INVENTORY)
                    .map(ClothesCaps::writeNBT)
                    .orElse(new CompoundTag());

            TRNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) target),
                    new SyncClothesCap(clothesTag, other.getId())
            );

        }
    }

}
