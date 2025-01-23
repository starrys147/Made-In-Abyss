package com.altnoir.mia.datagen;

import com.altnoir.mia.MIA;
import com.altnoir.mia.register.MIAItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MIAItemModelProvider extends ItemModelProvider {
    public MIAItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MIA.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(MIAItems.PURIN);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MIA.MOD_ID,"item/" + item.getId().getPath()));
    }
}
