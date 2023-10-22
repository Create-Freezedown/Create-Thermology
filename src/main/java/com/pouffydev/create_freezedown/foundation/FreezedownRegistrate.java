package com.pouffydev.create_freezedown.foundation;

import com.pouffydev.create_freezedown.Thermology;
import com.pouffydev.create_freezedown.content.fluids.boiler.SteamFluid;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.tterrag.registrate.builders.FluidBuilder;

public class FreezedownRegistrate extends CreateRegistrate {
    protected FreezedownRegistrate(String modid) {
        super(modid);
    }
    public static FreezedownRegistrate create(String modid) {
        return new FreezedownRegistrate(modid);
    }
    public FluidBuilder<SteamFluid, FreezedownRegistrate> steamFluid(String name) {
        return entry(name,
                c -> new VirtualFluidBuilder<SteamFluid, FreezedownRegistrate>(self(), this, name, c,
                        Thermology.asResource("fluid/" + name + "_still"), Thermology.asResource("fluid/" + name + "_flow"),
                        FreezedownRegistrate::defaultFluidType, SteamFluid::new));
    }
}
