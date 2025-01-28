package com.altnoir.mia.datagen;

import com.altnoir.mia.MIA;
import com.altnoir.mia.register.MIAItems;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MIAItemModelProvider extends ItemModelProvider {
    public MIAItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MIA.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MIAItems.PURIN.get());
    }
}
