package com.altnoir.mia.datagen;

import com.altnoir.mia.MIA;
import com.altnoir.mia.register.MIABlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class MIABlockStatesProvider extends BlockStateProvider {
    public MIABlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MIA.MOD_ID, exFileHelper);
    }

    @Override
    public void registerStatesAndModels() {
        blockWithItem(MIABlocks.EXAMPLE_BLOCK);
        blockWithItem(MIABlocks.EXAMPLE_PORTAL_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
