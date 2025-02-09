package com.altnoir.mia;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import static com.mojang.text2speech.Narrator.LOGGER;

public class EffectConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("mia/dimension_effects.json");
    private static final Map<ResourceLocation, List<EffectConfig>> DIMENSION_EFFECTS = new HashMap<>();

    public static class EffectConfig {
        public String id;
        public int duration;
        public int amplifier;
    }

    public static void loadConfig() {
        DIMENSION_EFFECTS.clear();
        try {
            if (!Files.exists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());
                Files.write(CONFIG_PATH, Collections.singletonList("[]"));
                return;
            }

            String json = new String(Files.readAllBytes(CONFIG_PATH), StandardCharsets.UTF_8);
            Type type = new TypeToken<List<DimensionEntry>>() {}.getType();
            List<DimensionEntry> entries = GSON.fromJson(json, type);

            for (DimensionEntry entry : entries) {
                ResourceLocation dimId = new ResourceLocation(entry.dimension);
                DIMENSION_EFFECTS.put(dimId, entry.effects);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load dimension effects config", e);
        }
    }

    public static List<EffectConfig> getEffects(ResourceLocation dimensionId) {
        return DIMENSION_EFFECTS.getOrDefault(dimensionId, Collections.emptyList());
    }

    public static class DimensionEntry {
        public String dimension;
        public List<EffectConfig> effects;

        // 必须添加无参构造器供Gson使用
        public DimensionEntry() {}
    }
}