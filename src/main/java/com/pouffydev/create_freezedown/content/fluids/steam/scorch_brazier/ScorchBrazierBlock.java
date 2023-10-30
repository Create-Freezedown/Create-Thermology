package com.pouffydev.create_freezedown.content.fluids.steam.scorch_brazier;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ScorchBrazierBlock extends Block implements IWrenchable, IBE<ScorchBrazierBlockEntity> {
    public ScorchBrazierBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<ScorchBrazierBlockEntity> getBlockEntityClass() {
        return null;
    }
    
    @Override
    public BlockEntityType<? extends ScorchBrazierBlockEntity> getBlockEntityType() {
        return null;
    }
}
