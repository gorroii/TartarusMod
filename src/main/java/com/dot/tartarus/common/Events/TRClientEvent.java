package com.dot.tartarus.common.Events;

import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Edited.IEditedProvider;
import com.dot.tartarus.common.UI.CharacterEditor;
import com.dot.tartarus.common.Utils.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TartarusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TRClientEvent {
    @SubscribeEvent
    public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        Player player = event.getPlayer();
        int editor = player.getCapability(IEditedProvider.Edited).map(editedcap -> editedcap.getEdited()).orElse(null);
        if(editor !=1){
            //Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new CharacterEditor()));
        }
    }


}
