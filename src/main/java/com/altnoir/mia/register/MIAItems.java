package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MIAItems {
    private static final DeferredRegister.Items REGISTER =
            DeferredRegister.createItems(MIA.MOD_ID);

    public static final DeferredItem<Item> PURIN = REGISTER.register("purin", Purin::new);

    public static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
