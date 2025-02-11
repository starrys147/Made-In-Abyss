package com.altnoir.mia.register;

import com.altnoir.mia.MIA;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MIATabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MIA.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MIA_TAB = REGISTER.register("mia",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(MIAItems.PURIN.get()))
                    .title(Component.translatable("itemGroup.mia.mia"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(Blocks.SPAWNER);
                        pOutput.accept(MIABlocks.CHLOROPHTRE_ORE.get());
                        pOutput.accept(MIABlocks.ALEXANDRITE_BLOCK.get());
                        pOutput.accept(MIAItems.PURIN.get());
                        pOutput.accept(MIAItems.ENDLESS_CUP.get());
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
