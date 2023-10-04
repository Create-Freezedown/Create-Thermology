package com.pouffydev.create_freezedown.content.kinetics.freezable.press;

import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FreezablePressBlockEntity extends MechanicalPressBlockEntity {
    public FreezablePressBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
