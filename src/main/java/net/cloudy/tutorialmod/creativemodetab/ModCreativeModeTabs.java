package net.cloudy.tutorialmod.creativemodetab;

import net.cloudy.tutorialmod.Tutorialmod;
import net.cloudy.tutorialmod.block.ModBlocks;
import net.cloudy.tutorialmod.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final CreativeModeTab ENDERITE_ITEM_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Tutorialmod.MOD_ID, "enderite_items"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.BLOCK_OF_ENDERITE))
                    .title(Component.translatable("creativemodetab.tutorialmod.enderite_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ENDERITE);
                        output.accept(ModItems.ENDERITE_NUGGET);
                        output.accept(ModItems.ENDERITE_WRAP);
                        output.accept(ModBlocks.BLOCK_OF_ENDERITE);
                        output.accept(ModBlocks.ENDERITE_ORE);
                        output.accept(ModItems.MAGIC_STICK);
                        output.accept(ModItems.MAGIC_BALL);

                    }).build());


    public static void registerModCreativeModeTabs() {
    Tutorialmod.LOGGER.info("Registering Creative Mode Tabs for " + Tutorialmod.MOD_ID);
    }






}
