package com.altnoir.mia.datagen;

import com.altnoir.mia.MIA;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = MIA.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new MIARecipesProvider(packOutput, lookupProvider));
        gen.addProvider(event.includeServer(), new MIABlockStatesProvider(packOutput, existingFileHelper));
        gen.addProvider(event.includeServer(), new MIAItemModelProvider(packOutput, existingFileHelper));
        gen.addProvider(event.includeServer(), new MIAWorldGenProvider(packOutput, lookupProvider));
    }
}
