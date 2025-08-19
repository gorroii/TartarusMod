package com.dot.tartarus.common.Utils;

import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinUtil {

    private static final Map<UUID, ResourceLocation> cache = new HashMap<>();

    // Manual draw order for slots
    private static final int[] DRAW_ORDER = { 3, 0, 5, 1, 2, 8, 7, 6, 4 };

    /**
     * Returns a dynamic player skin with overlays applied.
     * If cached, returns cached texture.
     */
    public static ResourceLocation getOrCreateSkin(UUID uuid, ResourceLocation baseSkin) {

        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        TextureManager tm = mc.getTextureManager();

        // Compute ResourceLocation
        ResourceLocation loc = new ResourceLocation("tartarus", "dynamic/skin_" + uuid.toString().replace("-", ""));

        // Delete old texture if exists
        if (tm.getTexture(loc) != null) {
            tm.release(loc);
        }

        try {
            // Load base skin
            NativeImage image = NativeImage.read(mc.getResourceManager()
                    .getResource(baseSkin).orElseThrow().open());

            //  Get clothes inventory from your capability
            player.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(cap -> {
                IItemHandler inv = cap.getInventory();

                for (int slot : DRAW_ORDER) {
                    ItemStack stack = inv.getStackInSlot(slot);
                    if (stack.isEmpty()) continue;

                    // Example: each clothing item has its own overlay texture
                    // You might want a custom system (like IClothingItem interface) instead
                    ResourceLocation overlayLoc = new ResourceLocation(
                            TartarusMod.MOD_ID,
                            "textures/entity/player/clothes/" + stack.getItem().getDescriptionId().replace("item.tartarus.", "") + ".png"
                    );

                    try (InputStream input = mc.getResourceManager().getResource(overlayLoc).orElseThrow().open()) {
                        NativeImage overlay = NativeImage.read(input);

                        int width = Math.min(image.getWidth(), overlay.getWidth());
                        int height = Math.min(image.getHeight(), overlay.getHeight());

                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                int overlayPixel = overlay.getPixelRGBA(x, y);

                                int a = (overlayPixel >> 24) & 0xFF;
                                if (a > 0) {
                                    image.setPixelRGBA(x, y, overlayPixel);
                                }
                            }
                        }

                        overlay.close();
                    } catch (Exception ignored) {
                        // if texture not found → skip silently
                    }
                }
            });

            // Create dynamic texture
            DynamicTexture dyn = new DynamicTexture(image);

            // Register in memory
            tm.register(loc, dyn);

            // Cache for future use
            cache.put(uuid, loc);

            return loc;

        } catch (Exception e) {
            e.printStackTrace();
            return baseSkin;
        }
    }

    /** Clears the cached texture for a specific player UUID */
    public static void ClearCache(Player player) {
        UUID uuid = player.getUUID();
        cache.remove(uuid);
    }
}