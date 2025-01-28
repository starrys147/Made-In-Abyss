package com.altnoir.mia.datagen;

import com.altnoir.mia.MIA;
import com.altnoir.mia.register.MIABlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MIABlockStatesProvider extends BlockStateProvider {
    public MIABlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MIA.MOD_ID, exFileHelper);
    }

    @Override
    public void registerStatesAndModels() {
        blockWithItem(MIABlocks.EXAMPLE_BLOCK);
        blockWithItem(MIABlocks.EXAMPLE_PORTAL_BLOCK);
        blockWithItem(MIABlocks.ALEXANDRITE_BLOCK);
    }

    private void blockWithItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
