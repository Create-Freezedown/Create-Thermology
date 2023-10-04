package com.pouffydev.create_freezedown.content.kinetics.freezable.press;

import com.pouffydev.create_freezedown.CTBlockEntityTypes;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FreezablePressBlock extends MechanicalPressBlock {
    public static final BooleanProperty frozen = BooleanProperty.create("frozen");
    public FreezablePressBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(frozen, false));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(frozen);
    }
    @Override
    public BlockEntityType<? extends FreezablePressBlockEntity> getBlockEntityType() {
        return CTBlockEntityTypes.press.get();
    }
}
