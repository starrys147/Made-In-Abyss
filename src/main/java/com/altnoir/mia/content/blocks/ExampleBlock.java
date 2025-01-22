package com.altnoir.mia.content.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ExampleBlock extends MIABaseBlock {
    public ExampleBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable());
    }
}
