package com.altnoir.mia.content.worldgen.biomemodifier;

import com.altnoir.mia.register.MIABiomeModifierConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.neoforged.neoforge.common.world.BiomeGenerationSettingsBuilder;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public record CarverAdder(HolderSet<Biome> biomes, HolderSet<ConfiguredWorldCarver<?>> carvers) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome)) {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            for(var carver : this.carvers) {
                generationSettings.addCarver(GenerationStep.Carving.AIR, carver);
            }
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return MIABiomeModifierConfig.CARVER_ADDER_CODEC;
    }
}
