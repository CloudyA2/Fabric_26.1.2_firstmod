package net.cloudy.tutorialmod;

import com.geckolib.animatable.SingletonGeoAnimatable;
import net.cloudy.tutorialmod.block.ModBlocks;
import net.cloudy.tutorialmod.creativemodetab.ModCreativeModeTabs;
import net.cloudy.tutorialmod.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tutorialmod implements ModInitializer {
	public static final String MOD_ID = "tutorialmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SingletonGeoAnimatable.registerSyncedAnimatable((SingletonGeoAnimatable) ModItems.MAGIC_STICK);
		ModCreativeModeTabs.registerModCreativeModeTabs();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

	}
}