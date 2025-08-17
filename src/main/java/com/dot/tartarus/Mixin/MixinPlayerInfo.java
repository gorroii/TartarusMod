package com.dot.tartarus.Mixin;

import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class MixinPlayerInfo {
    @Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
    private void setModelDefault(CallbackInfoReturnable<String> cir) {
        String type = "";
        PlayerInfo playerInfo = (PlayerInfo) (Object) this;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Player player = mc.level.getPlayerByUUID(playerInfo.getProfile().getId());
        if (player == null) return;
        int gender = player.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);
        if (gender == 1){
            type = "default";
        } else type = "slim";
        cir.setReturnValue(type);
    }
}
