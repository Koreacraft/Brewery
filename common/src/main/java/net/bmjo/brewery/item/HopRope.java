package net.bmjo.brewery.item;

import net.bmjo.brewery.entity.HopRopeKnotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class HopRope extends Item {

    public HopRope(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(BlockTags.FENCES) && player != null) {
            if (level.isClientSide) return InteractionResult.SUCCESS;
            InteractionHand hand = useOnContext.getHand();
            //try to get rope
            HopRopeKnotEntity knot = HopRopeKnotEntity.getHopRopeKnotEntity(level, blockPos);
            if (knot != null) {
                if (knot.interact(player, hand) == InteractionResult.CONSUME) {
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }
            //create new rope
            //HopRopeKnotEntity hopRopeKnotEntity = HopRopeKnotEntity.create(level, blockPos);
            knot = HopRopeKnotEntity.create(level, blockPos);
            knot.setTicksFrozen((byte) 0);
            level.addFreshEntity(knot);
            //wait because Entity has to exist
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return knot.interact(player, hand);
        } else {
            return InteractionResult.PASS;
        }
    }
}