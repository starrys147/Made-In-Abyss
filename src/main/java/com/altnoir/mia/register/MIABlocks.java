package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.blocks.ExampleBlock;
import com.altnoir.mia.content.blocks.ExamplePortalBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MIABlocks {
    private static final DeferredRegister<Block> REGISTER =
            DeferredRegister.create(BuiltInRegistries.BLOCK, MIA.MOD_ID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block", ExampleBlock::new);
    public static final DeferredBlock<Block> EXAMPLE_PORTAL_BLOCK = registerBlock("example_portal_block", ExamplePortalBlock::new);
    public static final DeferredBlock<Block> ALEXANDRITE_BLOCK = registerBlock("alexandrite_block",
    () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = (DeferredBlock<T>) REGISTER.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        MIAItems.registerBlockItem(name, block);
    }

    public static void register(IEventBus modEventBus){
        REGISTER.register(modEventBus);
    }
}
