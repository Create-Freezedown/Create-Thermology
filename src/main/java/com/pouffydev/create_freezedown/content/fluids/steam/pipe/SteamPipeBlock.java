package com.pouffydev.create_freezedown.content.fluids.steam.pipe;

import com.pouffydev.create_freezedown.CTBlockEntityTypes;
import com.pouffydev.create_freezedown.CTBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SteamPipeBlock extends FluidPipeBlock {
    public SteamPipeBlock(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (tryRemoveBracket(context))
            return InteractionResult.SUCCESS;

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();

        Direction.Axis axis = getAxis(world, pos, state);
        if (axis == null) {
            Vec3 clickLocation = context.getClickLocation()
                    .subtract(pos.getX(), pos.getY(), pos.getZ());
            double closest = Float.MAX_VALUE;
            Direction argClosest = Direction.UP;
            for (Direction direction : Iterate.directions) {
                if (clickedFace.getAxis() == direction.getAxis())
                    continue;
                Vec3 centerOf = Vec3.atCenterOf(direction.getNormal());
                double distance = centerOf.distanceToSqr(clickLocation);
                if (distance < closest) {
                    closest = distance;
                    argClosest = direction;
                }
            }
            axis = argClosest.getAxis();
        }

        if (clickedFace.getAxis() == axis)
            return InteractionResult.PASS;
        if (!world.isClientSide) {
            withBlockEntityDo(world, pos, fpte -> fpte.getBehaviour(FluidTransportBehaviour.TYPE).interfaces.values()
                    .stream()
                    .filter(pc -> pc != null && pc.hasFlow())
                    .findAny()
                    .ifPresent($ -> AllAdvancements.GLASS_PIPE.awardTo(context.getPlayer())));

            FluidTransportBehaviour.cacheFlows(world, pos);
            world.setBlockAndUpdate(pos, CTBlocks.glassSteamPipe.getDefaultState()
                    .setValue(GlassFluidPipeBlock.AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
            FluidTransportBehaviour.loadFlows(world, pos);
        }
        return InteractionResult.SUCCESS;
    }
    @Nullable
    private Direction.Axis getAxis(BlockGetter world, BlockPos pos, BlockState state) {
        return FluidPropagator.getStraightPipeAxis(state);
    }
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
                                       Player player) {
        return CTBlocks.steamPipe.asStack();
    }
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (!CTBlocks.boilerCasing.isIn(player.getItemInHand(hand)))
            return InteractionResult.PASS;
        if (world.isClientSide)
            return InteractionResult.SUCCESS;

        FluidTransportBehaviour.cacheFlows(world, pos);
        world.setBlockAndUpdate(pos,
                EncasedPipeBlock.transferSixWayProperties(state, CTBlocks.encasedSteamPipe.getDefaultState()));
        FluidTransportBehaviour.loadFlows(world, pos);
        return InteractionResult.SUCCESS;

    }
    @Override
    public Class<FluidPipeBlockEntity> getBlockEntityClass() {
        return FluidPipeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return CTBlockEntityTypes.steamPipe.get();
    }

}
