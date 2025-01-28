package com.altnoir.mia.datagen;

import com.altnoir.mia.register.MIABlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class MIARecipesProvider extends RecipeProvider implements IConditionBuilder {
    public MIARecipesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MIABlocks.EXAMPLE_BLOCK.get(), 1)
                .requires(MIABlocks.EXAMPLE_BLOCK.get())
                .unlockedBy(getHasName(MIABlocks.EXAMPLE_BLOCK.get()), has(MIABlocks.EXAMPLE_BLOCK.get()))
                .save(recipeOutput, "mia:example_block_from_example_block");
    }
}
