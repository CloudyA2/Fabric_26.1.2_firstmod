package net.cloudy.tutorialmod.item.custom;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class MagicItem extends Item implements GeoItem {

    public final MutableObject<GeoRenderProvider> geoRenderProvider = new MutableObject<>();
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("right_click");
    private static final RawAnimation ACTION1 = RawAnimation.begin().thenLoop("action1");

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
        Player player = context.getPlayer();
        BlockPos clickedPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        if (player != null && player.getCooldowns().isOnCooldown(context.getItemInHand())) {
            return InteractionResult.FAIL;
        }

        if (MAGIC_MAP.containsKey(clickedBlock)) {
            net.minecraft.server.level.ServerLevel serverLevel = level.isClientSide() ? null : (net.minecraft.server.level.ServerLevel) level;
            long animId = GeoItem.getOrAssignId(context.getItemInHand(), serverLevel);

            triggerAnim(player, animId, "IdleController", "action1");
        }

        if (level.isClientSide()) {
            // We are NOT on the server!
            return InteractionResult.CONSUME;
        }


        int radius = 1;
        Iterable<BlockPos> coordinates = BlockPos.betweenClosed(
                clickedPos.offset(-radius, -radius, -radius),
                clickedPos.offset(radius, radius, radius)
        );

        if (clickedBlock == Blocks.END_STONE) {
            handleEndStoneConversion(level, coordinates);
        } else if (MAGIC_MAP.containsKey(clickedBlock)) {
            handleOreConversion(level, coordinates, clickedBlock);
        }

        context.getItemInHand().hurtAndBreak(1, player, context.getHand());

        if (player != null) {
            player.getCooldowns().addCooldown(context.getItemInHand(), 13);
        }

        /*net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) level;*/

        //context.getItemInHand().hurtAndBreak(1, player, context.getHand());

        //level.setBlockAndUpdate(context.getClickedPos(), MAGIC_MAP.get(clickedBlock).defaultBlockState());

        Tutorialmod.LOGGER.info("Replaced/Clicked Pos: {}\nMAP: {}", context.getClickedPos(), MAGIC_MAP.get(clickedBlock).defaultBlockState());

        return InteractionResult.CONSUME;
    }

    // Helper method 1: adawfiahfiafwuwa
    private void handleEndStoneConversion(Level level, Iterable<BlockPos> coordinates) {
        List<BlockPos> validPositions = new ArrayList<>();
        for (BlockPos currentPos : coordinates) {
            if (level.getBlockState(currentPos).is(Blocks.END_STONE)) {
                validPositions.add(currentPos.immutable());
            }
        }

        Collections.shuffle(validPositions);
        int blocksToConvert = Math.min(5, validPositions.size());

        for (int i = 0; i < blocksToConvert; i++) {
            level.setBlockAndUpdate(validPositions.get(i), ModBlocks.ENDERITE_ORE.defaultBlockState());
        }
    }

    // Helper method 2: aaaaa
    private void handleOreConversion(Level level, Iterable<BlockPos> coordinates, Block clickedBlock) {
        Block replacementBlock = MAGIC_MAP.get(clickedBlock);
        for (BlockPos currentPos : coordinates) {
            if (level.getBlockState(currentPos).is(clickedBlock)) {
                level.setBlockAndUpdate(currentPos, replacementBlock.defaultBlockState());
            }
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("IdleController", 0, state -> {


            if (state.isCurrentAnimationStage("action1")) {
                return PlayState.CONTINUE;
            }

            return state.setAndContinue(IDLE_ANIM);
        })
                .triggerableAnim("action1", RawAnimation.begin()
                        .thenPlay("action1")
                        .thenLoop("right_click"))
                .triggerableAnim("idle", RawAnimation.begin().thenLoop("right_click")));
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
            private GeoItemRenderer<MagicItem> renderer;

            @Override
            public GeoItemRenderer<MagicItem> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new GeoItemRenderer<>(MagicItem.this);
                }
                return this.renderer;
            }
        });
    }
}
