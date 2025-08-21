package com.dot.tartarus.common.Caps.Clothes;

import com.dot.tartarus.Network.Packets.ClearCachePacket;
import com.dot.tartarus.Network.Packets.SyncClothesCap;
import com.dot.tartarus.Network.TRNetwork;
import com.dot.tartarus.common.Utils.PacketSyncUtils;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

public class ClothesCaps {

    private IItemHandler inventory = new ItemStackHandler(9){
        @Override
        protected void onContentsChanged(int slot) {
            if (player != null && !player.level().isClientSide) {
                // Данные текущего игрока → всем
                PacketSyncUtils.sendClothesToAll(player);
                PacketSyncUtils.sendCapabilitiesToAll(player);

                // Данные всех игроков → текущему
                PacketSyncUtils.sendAllClothesTo(player);
                PacketSyncUtils.sendAllCapabilitiesTo(player);

                // Сброс кеша скина
                TRNetwork.CHANNEL.send(
                        PacketDistributor.ALL.noArg(),
                        new ClearCachePacket(player.getUUID())
                );
            }
        }
    };
    private Player player;


    public ClothesCaps(Player player) {
        this.player = player;
    }

    public IItemHandler getInventory() {
        return inventory;
    }
    public void syncToAll(Level level) {
        level.players().forEach(playerEntity -> TRNetwork.CHANNEL.sendTo(new SyncClothesCap(writeNBT(), player.getId()), ((ServerPlayer) playerEntity).connection.connection, NetworkDirection.PLAY_TO_CLIENT));


    }




    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();

        CompoundTag inventoryCompound = new CompoundTag();

        for (int i = 0; i < inventory.getSlots(); i++) {
            inventoryCompound.put(String.valueOf(i), inventory.getStackInSlot(i).serializeNBT());
        }

        tag.put("inventory", inventoryCompound);

        return tag;
    }

    public void readNBT(Tag compound) {
        if(compound instanceof CompoundTag && ((CompoundTag) compound).contains("inventory")){
            CompoundTag inventoryCompound = ((CompoundTag) compound).getCompound("inventory");

            for (int i = 0; i < inventoryCompound.size(); i++) {
                ItemStack tempStack = ItemStack.of(inventoryCompound.getCompound(String.valueOf(i)));
                inventory.insertItem(i, tempStack, false); // always insert, triggers onContentsChanged
            }
        }
    }
}
