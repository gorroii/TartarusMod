package com.dot.tartarus.common.Utils;

import com.dot.tartarus.TartarusMod;
import com.dot.tartarus.common.Caps.Clothes.ClothesProvider;
import com.dot.tartarus.common.Caps.Clothes.ClothesStateProvider;
import com.dot.tartarus.common.Caps.Eyes.IEyeProvider;
import com.dot.tartarus.common.Caps.Gender.IGenderProvider;
import com.dot.tartarus.common.Caps.Hair.IHairProvider;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinUtil {

    private static final Map<UUID, ResourceLocation> cache = new HashMap<>();

    // Manual draw order for slots
    private static final int[] DRAW_ORDER = { 0, 7, 4, 3, 6, 2, 8, 1, 5 };

    /**
     * Returns a dynamic player skin with overlays applied.
     * If cached, returns cached texture.
     */
    public static ResourceLocation getOrCreateSkin(UUID uuid, ResourceLocation baseSkin) {

        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.level.getPlayerByUUID(uuid);

        if (player == null) {
            return baseSkin;
        }

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

            // --- APPLY EYE OVERLAY FIRST ---
            Integer eyeInt = player.getCapability(IEyeProvider.Eye)
                    .map(eyecap -> eyecap.getEye())
                    .orElse(null);

            if (eyeInt != null) {
                ResourceLocation eyeLoc = new ResourceLocation(
                        TartarusMod.MOD_ID,
                        "textures/entity/player/skin/eyes/eyes" + eyeInt + ".png"
                );
                applyOverlay(image, eyeLoc);
            }
            NativeImage hairimage = NativeImage.read(mc.getResourceManager()
                    .getResource(baseSkin).orElseThrow().open());

            // --- APPLY EYE OVERLAY FIRST ---
           Integer hairInt = player.getCapability(IHairProvider.Hair)
                    .map(eyecap -> eyecap.getHair())
                    .orElse(null);

            if (eyeInt != null) {
                ResourceLocation hairLoc = new ResourceLocation(
                        TartarusMod.MOD_ID,
                        "textures/entity/player/skin/hair/hair" + hairInt + ".png"
                );
                applyOverlay(image, hairLoc);
            }


            // --- APPLY CLOTHES ---
            player.getCapability(ClothesProvider.CLOTHES_INVENTORY).ifPresent(cap -> {
                IItemHandler inv = cap.getInventory();

                // get gender string once
                Integer genderInt = player.getCapability(IGenderProvider.Gender)
                        .map(gendercap -> gendercap.getGender())
                        .orElse(0); // fallback 0
                String genderStr = (genderInt == 1) ? "male" : "female";

                for (int slot : DRAW_ORDER) {
                    ItemStack stack = inv.getStackInSlot(slot);
                    if (stack.isEmpty()) continue;

                    Integer caps = stack.getCapability(ClothesStateProvider.State)
                            .map(statecap -> statecap.getState())
                            .orElse(null);
                    String capas = (caps != null && caps != 0) ? String.valueOf(caps) : "";

                    // choose path based on slot
                    ResourceLocation overlayLoc;
                    if (slot == 1 || slot == 5 || slot == 6) {
                        overlayLoc = new ResourceLocation(
                                TartarusMod.MOD_ID,
                                "textures/entity/player/clothes/" + genderStr + "/" +
                                        stack.getItem().getDescriptionId().replace("item.tartarus.", "") +
                                        capas + ".png"
                        );
                    } else {
                        overlayLoc = new ResourceLocation(
                                TartarusMod.MOD_ID,
                                "textures/entity/player/clothes/" +
                                        stack.getItem().getDescriptionId().replace("item.tartarus.", "") +
                                        capas + ".png"
                        );
                    }

                    applyOverlay(image, overlayLoc);
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

    /** Blends an overlay texture onto the base image */
    private static void applyOverlay(NativeImage base, ResourceLocation overlayLoc) {
        Minecraft mc = Minecraft.getInstance();
        try (InputStream input = mc.getResourceManager().getResource(overlayLoc).orElseThrow().open()) {
            NativeImage overlay = NativeImage.read(input);

            int width = Math.min(base.getWidth(), overlay.getWidth());
            int height = Math.min(base.getHeight(), overlay.getHeight());

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int overlayPixel = overlay.getPixelRGBA(x, y);
                    int basePixel = base.getPixelRGBA(x, y);

                    int aO = (overlayPixel >> 24) & 0xFF;
                    if (aO == 0) continue;

                    int rO = (overlayPixel >> 16) & 0xFF;
                    int gO = (overlayPixel >> 8) & 0xFF;
                    int bO = overlayPixel & 0xFF;

                    int aB = (basePixel >> 24) & 0xFF;
                    int rB = (basePixel >> 16) & 0xFF;
                    int gB = (basePixel >> 8) & 0xFF;
                    int bB = basePixel & 0xFF;

                    float alphaO = aO / 255f;
                    float alphaB = aB / 255f;

                    float outA = alphaO + alphaB * (1 - alphaO);
                    float outR = (rO * alphaO + rB * alphaB * (1 - alphaO)) / outA;
                    float outG = (gO * alphaO + gB * alphaB * (1 - alphaO)) / outA;
                    float outB = (bO * alphaO + bB * alphaB * (1 - alphaO)) / outA;

                    int blended =
                            ((int) (outA * 255) << 24) |
                                    ((int) (outR) << 16) |
                                    ((int) (outG) << 8) |
                                    ((int) (outB));

                    base.setPixelRGBA(x, y, blended);
                }
            }

            overlay.close();
        } catch (Exception ignored) {
            // texture not found → skip
        }
    }
}