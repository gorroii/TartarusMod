package com.dot.tartarus.Mixin;

import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Skin.ISkinProvider;
import com.dot.tartarus.common.Caps.Skin.SkinUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends Player {
    public MixinAbstractClientPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }
    /**
     * @author Dot
     * @reason To change where it fetch the skin texture
     */
    @Overwrite
    public ResourceLocation getSkinTextureLocation(){
        Player player = Minecraft.getInstance().player;
        ResourceLocation location = null;
        int gender = player.getCapability(IGenderProvider.Gender).map(gendercap -> gendercap.getGender()).orElse(null);
        int skin = player.getCapability(ISkinProvider.Skin).map(gendercap -> gendercap.getSkin()).orElse(null);


        HashMap<Integer, ResourceLocation> femalemap = new HashMap<Integer, ResourceLocation>();
        femalemap.put(0, new ResourceLocation("tartarus:textures/entity/player/skin/random_skin.png"));
        femalemap.put(1, new ResourceLocation("tartarus:textures/entity/player/skin/female/female1.png"));
        femalemap.put(2, new ResourceLocation("tartarus:textures/entity/player/skin/female/female2.png"));
        femalemap.put(3, new ResourceLocation("tartarus:textures/entity/player/skin/female/female3.png"));
        femalemap.put(4, new ResourceLocation("tartarus:textures/entity/player/skin/female/female4.png"));

        HashMap<Integer, ResourceLocation> malemap = new HashMap<Integer, ResourceLocation>();
        malemap.put(0, new ResourceLocation("tartarus:textures/entity/player/skin/random_skin.png"));
        malemap.put(1, new ResourceLocation("tartarus:textures/entity/player/skin/male/male1.png"));
        malemap.put(2, new ResourceLocation("tartarus:textures/entity/player/skin/male/male2.png"));
        malemap.put(3, new ResourceLocation("tartarus:textures/entity/player/skin/male/male3.png"));
        malemap.put(4, new ResourceLocation("tartarus:textures/entity/player/skin/male/male4.png"));
        switch(gender){
            case 1:
                location = malemap.get(skin);
                break;
            case 2:
                location = femalemap.get(skin);
                break;
        }


        return SkinUtil.getOrCreateSkin(player.getUUID(), location);
    }
}
