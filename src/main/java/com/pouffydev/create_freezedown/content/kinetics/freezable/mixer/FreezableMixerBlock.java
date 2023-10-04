package com.pouffydev.create_freezedown.content.kinetics.freezable.mixer;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FreezableMixerBlock extends MechanicalMixerBlock {
    public static final BooleanProperty frozen = BooleanProperty.create("frozen");
    public boolean isFrozen(BlockState state) {
        return state.getValue(frozen);
    }
    public FreezableMixerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(frozen, false));
    }
    @Override
    public BlockEntityType<? extends MechanicalMixerBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.MECHANICAL_MIXER.get();
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(frozen);
    }
}
