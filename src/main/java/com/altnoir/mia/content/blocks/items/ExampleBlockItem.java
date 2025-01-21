package com.altnoir.mia.content.blocks.items;

import com.altnoir.mia.register.MIABlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ExampleBlockItem extends MIABaseBlockItem{
    public ExampleBlockItem() {
        super(MIABlocks.EXAMPLE_BLOCK.get(), new Item.Properties());
    }
}
