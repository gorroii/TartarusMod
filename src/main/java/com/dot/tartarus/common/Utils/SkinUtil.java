package com.dot.tartarus.common.Utils;

import com.dot.tartarus.TartarusMod;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinUtil {

    private static final Map<UUID, ResourceLocation> cache = new HashMap<>();

    /**
     * Returns a dynamic player skin with overlay applied.
     * If the UUID is cached, returns cached texture.
     * If not cached, removes old texture if it exists and regenerates.
     */
    public static ResourceLocation getOrCreateSkin(UUID uuid, ResourceLocation baseSkin) {

        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        Minecraft mc = Minecraft.getInstance();
        TextureManager tm = mc.getTextureManager();

        // compute ResourceLocation
        ResourceLocation loc = new ResourceLocation("tartarus", "dynamic/skin_" + uuid.toString().replace("-", ""));

        // delete old texture if exists
        if (tm.getTexture(loc) != null) {
            tm.release(loc);
        }

        try {
            // load base skin
            NativeImage image = NativeImage.read(mc.getResourceManager()
                    .getResource(baseSkin).orElseThrow().open());

            // load overlay (e.g., implants, tattoos, clothes)
            NativeImage overlay = NativeImage.read(mc.getResourceManager()
                    .getResource(new ResourceLocation(TartarusMod.MOD_ID, "textures/entity/player/implants/random.png")).orElseThrow().open());

            int width = Math.min(image.getWidth(), overlay.getWidth());
            int height = Math.min(image.getHeight(), overlay.getHeight());

            // overlay pixels
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int overlayPixel = overlay.getPixelRGBA(x, y);

                    int a = (overlayPixel >> 24) & 0xFF;
                    int r = (overlayPixel >> 16) & 0xFF;
                    int g = (overlayPixel >> 8) & 0xFF;
                    int b = overlayPixel & 0xFF;

                    if (a > 0) {
                        // replace pixel with overlay (or blend if you want)
                        int newPixel = (a << 24) | (r << 16) | (g << 8) | b;
                        image.setPixelRGBA(x, y, newPixel);
                    }
                }
            }

            // create dynamic texture
            DynamicTexture dyn = new DynamicTexture(image);

            // register in memory
            tm.register(loc, dyn);

            // cache for future use
            cache.put(uuid, loc);

            return loc;

        } catch (Exception e) {
            e.printStackTrace();
            return baseSkin;
        }
    }

    /** Clears the cached texture for a specific player UUID */
    public static void ClearCache(Player player){
        UUID uuid = player.getUUID();
        cache.remove(uuid);
    }
}
