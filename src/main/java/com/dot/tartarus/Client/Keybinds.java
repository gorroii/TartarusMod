package com.dot.tartarus.Client;

import com.dot.tartarus.TartarusMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class Keybinds {

    public static final KeyMapping OPEN_INVENTORY = new KeyMapping("key." + TartarusMod.MOD_ID + ".open_inventory", GLFW.GLFW_KEY_C, "key." + TartarusMod.MOD_ID + ".category");

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.OPEN_INVENTORY);
    }
}
