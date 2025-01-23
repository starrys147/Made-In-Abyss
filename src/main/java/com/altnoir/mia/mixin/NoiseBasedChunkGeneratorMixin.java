package com.altnoir.mia.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {
    private static final Logger made_In_Abyss$LOGGER = LogUtils.getLogger();

    @Inject(method = "doFill", at = @At("TAIL"))
    private void generateCentralHole(Blender pBlender, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk, int pMinCellY, int pCellCountY, CallbackInfoReturnable<ChunkAccess> cir) {
        //made_In_Abyss$LOGGER.debug("ok");
        //HoleGenerator.LOGGER.debug("ok?");
    }
}
