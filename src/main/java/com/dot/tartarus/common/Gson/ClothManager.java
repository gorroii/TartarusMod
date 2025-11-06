package com.dot.tartarus.common.Gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ClothManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<Item, Integer> STATES = new HashMap<>();

    public ClothManager() {
        super(GSON, "cloth_params"); // folder inside "data/namespace/"
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        STATES.clear();

        map.forEach((id, json) -> {
            try {
                int number = json.getAsJsonObject().get("max_states").getAsInt();


                // resolve item from id namespace:path
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(id.getNamespace(), id.getPath()));
                if (item != null) {
                    STATES.put(item, number);
                }
            } catch (Exception e) {
                System.err.println("Failed to parse item_numbers for " + id + ": " + e);
            }
        });
    }

    public static int getNumber(Item item) {
        return STATES.getOrDefault(item, 0);
    }
}