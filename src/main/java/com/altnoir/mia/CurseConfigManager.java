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

public class CurseConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("mia/dimension_curse.json");
    private static final Map<ResourceLocation, List<EffectConfig>> DIMENSION_CURSE = new HashMap<>();

    public static class EffectConfig {
        public String id;
        public int duration;
        public int amplifier;
    }

    public static void loadConfig() {
        DIMENSION_CURSE.clear();
        try {
            if (!Files.exists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());
                // 创建默认配置的JSON内容
                List<DimensionEntry> defaultEntries = new ArrayList<>();

                // 示例配置
                DimensionEntry alluringForest = new DimensionEntry();
                alluringForest.dimension = "mia:alluring_forest";
                alluringForest.effects = new ArrayList<>();
                EffectConfig nausea = new EffectConfig();
                nausea.id = "minecraft:nausea";
                nausea.duration = 300;
                nausea.amplifier = 0;
                alluringForest.effects.add(nausea);
                // 示例配置
                DimensionEntry theEnd = new DimensionEntry();
                theEnd.dimension = "minecraft:the_end";
                theEnd.effects = new ArrayList<>();
                EffectConfig blindness = new EffectConfig();
                blindness.id = "minecraft:darkness";
                blindness.duration = 600;
                blindness.amplifier = 0;
                EffectConfig slowness = new EffectConfig();
                slowness.id = "minecraft:slowness";
                slowness.duration = 600;
                slowness.amplifier = 2;
                theEnd.effects.add(blindness);
                theEnd.effects.add(slowness);

                defaultEntries.add(alluringForest);
                defaultEntries.add(theEnd);

                // 将默认配置写入文件
                String defaultJson = GSON.toJson(defaultEntries);
                Files.write(CONFIG_PATH, defaultJson.getBytes(StandardCharsets.UTF_8));
                return;
            }

            String json = new String(Files.readAllBytes(CONFIG_PATH), StandardCharsets.UTF_8);
            Type type = new TypeToken<List<DimensionEntry>>() {}.getType();
            List<DimensionEntry> entries = GSON.fromJson(json, type);

            for (DimensionEntry entry : entries) {
                ResourceLocation dimId = new ResourceLocation(entry.dimension);
                DIMENSION_CURSE.put(dimId, entry.effects);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load dimension curse config", e);
        }
    }

    public static List<EffectConfig> getEffects(ResourceLocation dimensionId) {
        return DIMENSION_CURSE.getOrDefault(dimensionId, Collections.emptyList());
    }

    public static class DimensionEntry {
        public String dimension;
        public List<EffectConfig> effects;

        // 必须添加无参构造器供Gson使用
        public DimensionEntry() {}
    }
}