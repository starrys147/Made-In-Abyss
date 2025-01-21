package com.altnoir.mia.content.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ExampleBlock extends MIABaseBlock {
    public ExampleBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    }
}
