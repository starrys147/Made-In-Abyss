package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MIATabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MIA.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MIA_TAB = REGISTER.register("mia",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(MIAItems.PURIN.get()))
                    .title(Component.translatable("itemGroup.mia.mia"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(Blocks.SPAWNER);
                        pOutput.accept(MIABlocks.ALEXANDRITE_BLOCK.get());
                        pOutput.accept(MIAItems.PURIN.get());
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
