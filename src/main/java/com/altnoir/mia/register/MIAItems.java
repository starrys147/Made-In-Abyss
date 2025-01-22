package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import com.altnoir.mia.content.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MIAItems {
    private static final DeferredRegister<Item> REGISTER =
            DeferredRegister.create(ForgeRegistries.ITEMS, MIA.MOD_ID);

    public static final RegistryObject<Item> EXAMPLE_ITEM = REGISTER.register("example_item", ExampleItem::new);

    public static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus modEventBus){
        REGISTER.register(modEventBus);
    }
}
