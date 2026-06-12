package net.cloudy.tutorialmod.item;

import net.cloudy.tutorialmod.Tutorialmod;
import net.cloudy.tutorialmod.item.custom.MagicItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item ENDERITE = registerItem("enderite", Item::new);
    public static final Item ENDERITE_WRAP = registerItem("enderite_wrap", Item::new);
    public static final Item ENDERITE_NUGGET = registerItem("enderite_nugget", Item::new);

    public static final Item MAGIC_STICK = registerItem("magic_stick", properties -> new MagicItem(properties.durability(200)));



    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Tutorialmod.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Tutorialmod.MOD_ID, name)))));
    }


    public static void registerModItems() {
        Tutorialmod.LOGGER.info("Registering Mod Items for " + Tutorialmod.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(fabricCreativeModeTabOutput -> {
            fabricCreativeModeTabOutput.accept(ENDERITE);
            fabricCreativeModeTabOutput.accept(ENDERITE_WRAP);
            fabricCreativeModeTabOutput.accept(ENDERITE_NUGGET);
        });
    }
}
