package com.dot.tartarus.Network.Packets;

import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Caps.Clothes.ClothesStateProvider;
import com.dot.tartarus.common.Gson.MaxStateClothManager;
import com.dot.tartarus.common.Sounds.TRSounds;
import com.dot.tartarus.common.Utils.PacketSyncUtils;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.UUID;
import java.util.function.Supplier;

public class SendStateChangePacket  {


    public SendStateChangePacket() {

    }

    public static void encode(SendStateChangePacket msg, FriendlyByteBuf buf) {

    }

    public static SendStateChangePacket decode(FriendlyByteBuf buf) {
        return new SendStateChangePacket();
    }

    public static void handle(SendStateChangePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ServerPlayer player = ctx.get().getSender();

            player.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(cap -> {
                IItemHandler inv = cap.getInventory();
                ItemStack item4 = inv.getStackInSlot(4);
                ItemStack item5 = inv.getStackInSlot(5);
                if (item5 != ItemStack.EMPTY){

                    int state = item5.getCapability(ClothesStateProvider.State).map(statecap -> statecap.getState()).orElse(null);
                    if (state < MaxStateClothManager.getNumber(item5.getItem())) {
                        item5.getCapability(ClothesStateProvider.State).ifPresent(statecap -> statecap.setState(state + 1));
                        TRNetwork.CHANNEL.sendToServer(new ServerSoundPacket(TRSounds.UNZIP.get()));


                    }
                    if (state == MaxStateClothManager.getNumber(item5.getItem())){
                        item5.getCapability(ClothesStateProvider.State).ifPresent(statecap -> statecap.setState(0));
                        if(MaxStateClothManager.getNumber(item5.getItem())!= 0){
                            TRNetwork.CHANNEL.sendToServer(new ServerSoundPacket(TRSounds.ZIP.get()));
                        }
                    }
                } else {
                    if (item4 != ItemStack.EMPTY){
                        int state = item4.getCapability(ClothesStateProvider.State).map(statecap -> statecap.getState()).orElse(null);
                        if (state < MaxStateClothManager.getNumber(item4.getItem())) {
                            item4.getCapability(ClothesStateProvider.State).ifPresent(statecap -> statecap.setState(state + 1));

                        }
                        if (state == MaxStateClothManager.getNumber(item4.getItem())){
                            item4.getCapability(ClothesStateProvider.State).ifPresent(statecap -> statecap.setState(0));

                        }

                    }


                }
                if (player != null && !player.level().isClientSide) {
                    // Данные текущего игрока → всем
                    PacketSyncUtils.sendClothesToAll(player);

                    // Сброс кеша скина
                    TRNetwork.CHANNEL.send(
                            PacketDistributor.ALL.noArg(),
                            new ClearCachePacket(player.getUUID())
                    );
                }

            });


            });
        ctx.get().setPacketHandled(true);
    }
}