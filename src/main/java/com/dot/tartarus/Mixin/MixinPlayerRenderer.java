package com.dot.tartarus.Mixin;

import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Gender.IGender;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.ibm.icu.util.GenderInfo;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer {

    public MixinPlayerRenderer(EntityRendererProvider.Context pContext, EntityModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    public void getTexture(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> ci) {
       int gender = player.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);

        HashMap<Integer, ResourceLocation> map = new HashMap<Integer, ResourceLocation>();
        map.put(0, new ResourceLocation("tartarus:textures/entity/player/skin/random_skin.png"));
        map.put(1, new ResourceLocation("tartarus:textures/entity/player/skin/male.png"));
        map.put(2, new ResourceLocation("tartarus:textures/entity/player/skin/female.png"));


        ci.setReturnValue(map.get(gender));
    }
}