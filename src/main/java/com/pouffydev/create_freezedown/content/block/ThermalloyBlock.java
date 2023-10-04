package com.pouffydev.create_freezedown.content.block;

import com.pouffydev.create_freezedown.content.block.util.BlockTemperature;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ThermalloyBlock extends Block {
    public static final EnumProperty<BlockTemperature> heatLevel = EnumProperty.create("temperature", BlockTemperature.class);
    public ThermalloyBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(heatLevel, BlockTemperature.NORMAL));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(heatLevel);
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        return state.setValue(heatLevel, BlockTemperature.NORMAL);
    }
}
