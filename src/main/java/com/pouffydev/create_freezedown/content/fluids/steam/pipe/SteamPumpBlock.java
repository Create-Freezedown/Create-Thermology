package com.pouffydev.create_freezedown.content.fluids.steam.pipe;


import com.pouffydev.create_freezedown.CTBlockEntityTypes;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteamPumpBlock extends PumpBlock {
    public SteamPumpBlock(Properties p_i48415_1_) {
        super(p_i48415_1_);
    }


    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        super.tick(state,world,pos,r);
        this.getBlockEntity(world,pos).updatePressureChange();

    }

    @Override
    public Class<PumpBlockEntity> getBlockEntityClass() {
        return PumpBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return CTBlockEntityTypes.steamPump.get();
    }
}