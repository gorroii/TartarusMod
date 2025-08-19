package com.dot.tartarus.common.Caps.Clothes;

import com.dot.tartarus.Network.Packets.SyncClothesCap;
import com.dot.tartarus.Network.TRNetwork;
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

public class ClothesCaps {

    private IItemHandler inventory = new ItemStackHandler(9){
        @Override
        protected void onContentsChanged(int slot) {
            if (player != null && !player.level().isClientSide) {
                syncToAll(player.level());
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
        SkinUtil.ClearCache(player);

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
                if (tempStack.getItem() == inventory.getStackInSlot(i).getItem()) {
                    inventory.getStackInSlot(i).deserializeNBT(inventoryCompound.getCompound(String.valueOf(i)));
                } else
                    inventory.insertItem(i, tempStack, false);

            }
        }
    }
}
