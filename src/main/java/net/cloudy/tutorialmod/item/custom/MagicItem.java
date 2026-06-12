package net.cloudy.tutorialmod.item.custom;

import net.cloudy.tutorialmod.Tutorialmod;
import net.cloudy.tutorialmod.block.ModBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.logging.Logger;

public class MagicItem extends Item {
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
}
