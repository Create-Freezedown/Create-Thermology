package com.pouffydev.create_freezedown.content.fluids.steam;

import com.pouffydev.create_freezedown.CTFluids;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraftforge.fluids.FluidStack;

public interface ISteamConsumingBE {
    default boolean hasSteam(FluidStack fluid) {
        return fluid.getFluid() instanceof SteamFluid;
    }
    FluidStack steam = new FluidStack(FluidHelper.convertToStill(CTFluids.steam.get()), 1);
    
    default boolean isSteamSuperheated(FluidStack fluid) {
        return fluid.getFluid() instanceof SuperheatedSteamFluid;
    }
}
