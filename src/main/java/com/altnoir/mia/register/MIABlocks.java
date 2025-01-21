package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.blocks.ExampleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MIABlocks {
    private static final DeferredRegister<Block> REGISTER =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MIA.MOD_ID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK = REGISTER.register("example_block", ExampleBlock::new);

    public static void register(IEventBus modEventBus){
        REGISTER.register(modEventBus);
    }
}
