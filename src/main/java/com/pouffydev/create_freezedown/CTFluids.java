package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.content.fluids.steam.SteamFluid;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.FluidEntry;

import static com.pouffydev.create_freezedown.Thermology.REGISTRATE;


public class CTFluids {
    public static final FluidEntry<SteamFluid> steam = REGISTRATE.steamFluid("steam")
            .lang("Steam")
            .properties(b -> b.density(-590))
            .tag(AllTags.forgeFluidTag("steam"))
            .register();
    
    public static void register() {}
}
