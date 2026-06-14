package net.cloudy.tutorialmod.datagen;

import net.cloudy.tutorialmod.block.ModBlocks;
import net.cloudy.tutorialmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.ItemLike;
import org.lwjgl.system.macosx.MacOSXLibraryDL;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
                List<ItemLike> ENDERITE_SMELTABLES = List.of(ModBlocks.ENDERITE_ORE);

                oreSmelting(ENDERITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.ENDERITE_NUGGET, 1f, 200, "enderite");
                oreBlasting(ENDERITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.ENDERITE_NUGGET, 1f, 100, "enderite");

                nineBlockStorageRecipes(RecipeCategory.BUILDING_BLOCKS, ModItems.ENDERITE, RecipeCategory.DECORATIONS, ModBlocks.BLOCK_OF_ENDERITE);

                shaped(RecipeCategory.MISC, ModItems.ENDERITE_WRAP)
                        .pattern(" RR")
                        .pattern("RRR")
                        .pattern("RR ")
                        .define('R', ModItems.ENDERITE_NUGGET)
                        .unlockedBy(getHasName(ModItems.ENDERITE_NUGGET), has(ModItems.ENDERITE_NUGGET))
                        .group("enderite")
                        .save(output);

                shaped(RecipeCategory.MISC, ModItems.ENDERITE)
                        .pattern(" R ")
                        .pattern("RNR")
                        .pattern(" R ")
                        .define('R', ModItems.ENDERITE_WRAP)
                        .define('N', Items.NETHERITE_INGOT)
                        .unlockedBy(getHasName(ModItems.ENDERITE_WRAP), has(ModItems.ENDERITE_WRAP))
                        .group("enderite")
                        .save(output, "enderite_from_crafting");

                shaped(RecipeCategory.MISC, ModItems.MAGIC_STICK)
                        .pattern("I")
                        .pattern("R")
                        .define('R', Items.BREEZE_ROD)
                        .define('I', ModItems.MAGIC_BALL)
                        .unlockedBy(getHasName(ModItems.MAGIC_BALL), has(ModItems.MAGIC_BALL))
                        .group("enderite")
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.ENDERITE)
                        .requires(ModBlocks.BLOCK_OF_ENDERITE)
                        .unlockedBy(getHasName(ModBlocks.BLOCK_OF_ENDERITE), has(ModBlocks.BLOCK_OF_ENDERITE))
                        .group("enderite")
                        .save(output, "enderite_from_block_of_enderite");


            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
