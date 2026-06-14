package net.cloudy.tutorialmod.item.custom;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.cache.animation.Animation;
import com.geckolib.constant.DefaultAnimations;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.util.GeckoLibUtil;
import com.google.common.base.Suppliers;
import com.llamalad7.mixinextras.lib.apache.commons.mutable.MutableObject;
import net.cloudy.tutorialmod.Tutorialmod;
import net.cloudy.tutorialmod.block.ModBlocks;
import net.cloudy.tutorialmod.item.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class MagicItem extends Item implements GeoItem {

    public final MutableObject<GeoRenderProvider> geoRenderProvider = new MutableObject<>();
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("right_click");

    private static final Map<Block, Block> MAGIC_MAP =
            Map.of(
                    Blocks.END_STONE, ModBlocks.ENDERITE_ORE,
                    Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK,
                    ModBlocks.ENDERITE_ORE, Blocks.END_STONE
            );


    public MagicItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        //right click block
        //change block from a to b

        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        if(MAGIC_MAP.containsKey(clickedBlock) && !level.isClientSide()) {
            // We are on the server!
            level.setBlockAndUpdate(context.getClickedPos(), MAGIC_MAP.get(clickedBlock).defaultBlockState());
            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), context.getHand());

            Tutorialmod.LOGGER.info("Replaced/Clicked Pos: " + context.getClickedPos() + "\nMAP: " + MAGIC_MAP.get(clickedBlock).defaultBlockState());

        }


        return InteractionResult.SUCCESS;

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("IdleController", 0, state -> {
            // Force the idle animation to continuously loop unconditionally
            return state.setAndContinue(IDLE_ANIM);
        }).setTransitionTicks(20));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {

        new GeoItemRenderer<>(MagicItem.this);

        consumer.accept(this.geoRenderProvider.getValue());

        consumer.accept(new GeoRenderProvider() {
            private final Supplier<GeoItemRenderer<MagicItem>> renderer = Suppliers.memoize(() -> new GeoItemRenderer<>(MagicItem.this));

            @Override
            public @Nullable GeoItemRenderer<MagicItem> getGeoItemRenderer() {
                return this.renderer.get();
            }
        });
    }
}
