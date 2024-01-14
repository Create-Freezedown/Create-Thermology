package com.pouffydev.create_freezedown.content.fluids.steam.scorch_brazier;

import com.pouffydev.create_freezedown.content.fluids.steam.IHeatEmittingBE;
import com.pouffydev.create_freezedown.content.fluids.steam.ISteamConsumingBE;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public class ScorchBrazierBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IHeatEmittingBE, ISteamConsumingBE {
    protected SmartFluidTankBehaviour tank;
    
    public ScorchBrazierBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(tank);
    }
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side == Direction.DOWN)
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }
    @Override
    public boolean shouldEmmitHeat() {
        SmartFluidTank fluidHandler = tank.getPrimaryHandler();
        return hasSteam(fluidHandler.getFluid());
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean fluid = containedFluidTooltip(tooltip, isPlayerSneaking,
                getCapability(ForgeCapabilities.FLUID_HANDLER));
        if (fluid)
            tooltip.add(Components.immutableEmpty());
        return temperatureOutputTooltip(tooltip, isPlayerSneaking, getTempModifier());
    }
    private int getRadius() {
        return 5;
    }
    private int getTempModifier() {
        SmartFluidTank fluidHandler = tank.getPrimaryHandler();
        if (fluidHandler.getFluid().containsFluid(steam))
            return 20;
        return 0;
    }
    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide)
            return;
        changeTemp(level, worldPosition, getRadius(), getTempModifier());
    }
}
