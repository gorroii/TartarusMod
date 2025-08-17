package com.dot.tartarus.common.Commands;

import com.dot.tartarus.Network.Packets.GenderPacket;
import com.dot.tartarus.Network.Packets.HairPacket;
import com.dot.tartarus.Network.Packets.SkinPacket;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Utils.SkinUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import static com.dot.tartarus.Network.Packets.PacketSyncUtils.sendCapabilitiesToAll;

public class SetCharacterCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("character")
                        .requires(cs -> cs.hasPermission(4))

                        // Gender branch
                        .then(Commands.literal("gender")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(1, 2))
                                                        .executes(SetCharacterCommand::setGender)
                                                )
                                        )
                                )
                        )

                        // Hair branch
                        .then(Commands.literal("hair")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 100))
                                                        .executes(SetCharacterCommand::setHair)
                                                )
                                        )
                                )
                        )

                        // Skin branch
                        .then(Commands.literal("skin")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 100))
                                                        .executes(SetCharacterCommand::setSkin)
                                                )
                                        )
                                )
                        )
        );
    }

    private static int setGender(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        int value = IntegerArgumentType.getInteger(context, "value");
        SkinUtil.ClearCache(player);
        player.getCapability(IGenderProvider.Gender).ifPresent(cap -> cap.setGender(value));
        TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new GenderPacket(value));
        sendCapabilitiesToAll(player);

        return 1;
    }

    private static int setHair(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        int value = IntegerArgumentType.getInteger(context, "value");
        SkinUtil.ClearCache(player);
        player.getCapability(IHairProvider.Hair).ifPresent(cap -> cap.setHair(value));
        TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new HairPacket(value));
        sendCapabilitiesToAll(player);

        return 1;
    }

    private static int setSkin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        int value = IntegerArgumentType.getInteger(context, "value");
        SkinUtil.ClearCache(player);
         player.getCapability(ISkinProvider.Skin).ifPresent(cap -> cap.setSkin(value));

        TRNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SkinPacket(value));
        sendCapabilitiesToAll(player);


        return 1;
    }
}