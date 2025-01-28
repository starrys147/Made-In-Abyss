package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.worldgen.biomemodifier.CarverAdder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MIABiomeModifierConfig {
    public static DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MIA.MOD_ID);

    public static DeferredHolder<MapCodec<CarverAdder>> CARVER_ADDER_CODEC = BIOME_MODIFIER_SERIALIZERS.register("carver_adder", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    // declare fields
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(CarverAdder::biomes),
                    ConfiguredWorldCarver.LIST_CODEC.fieldOf("carvers").forGetter(CarverAdder::carvers)
                    // declare constructor
            ).apply(builder, CarverAdder::new)));

    public static void register(IEventBus modEventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
    }
}
