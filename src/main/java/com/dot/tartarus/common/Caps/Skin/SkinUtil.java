package com.dot.tartarus.common.Caps.Skin;

import com.dot.tartarus.TartarusMod;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinUtil {
    private static final Map<UUID, ResourceLocation> cache = new HashMap<>();

    public static ResourceLocation getOrCreateSkin(UUID uuid, ResourceLocation baseSkin) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        Minecraft mc = Minecraft.getInstance();


        try {
            NativeImage image = NativeImage.read(mc.getResourceManager()
                    .getResource(baseSkin).orElseThrow().open());
            NativeImage torso_cloth = NativeImage.read(mc.getResourceManager()
                    .getResource(new ResourceLocation(TartarusMod.MOD_ID, "textures/entity/player/implants/random.png")).orElseThrow().open());
            // Example: paint skin solid red
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int pixel = (torso_cloth.getPixelRGBA(x,y));
                    Color c = new Color(pixel);
                    int alpha = c.getAlpha();


                    if (alpha < 255){
                        break;
                    } else {
                        image.setPixelRGBA(x, y, pixel);
                    }
                }
            }


            DynamicTexture dyn = new DynamicTexture(image);
            ResourceLocation loc = new ResourceLocation("tartarus", "dynamic/skin_" + uuid.toString().replace("-", ""));
            mc.getTextureManager().register(loc, dyn);

            cache.put(uuid, loc);
            return loc;
        } catch (Exception e) {
            e.printStackTrace();
            return baseSkin;
        }
    }
}
