package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankBlockEntity;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankRenderer;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankBlockEntity;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankRenderer;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankBlockEntity;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankInstance;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankRenderer;
import com.pouffydev.create_freezedown.content.kinetics.freezable.mixer.FreezableMixerBlockEntity;
import com.pouffydev.create_freezedown.content.kinetics.freezable.mixer.FreezableMixerInstance;
import com.pouffydev.create_freezedown.content.kinetics.freezable.mixer.FreezableMixerRenderer;
import com.pouffydev.create_freezedown.content.kinetics.freezable.press.FreezablePressBlockEntity;
import com.pouffydev.create_freezedown.content.kinetics.freezable.press.FreezablePressInstance;
import com.pouffydev.create_freezedown.content.kinetics.freezable.press.FreezablePressRenderer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpCogInstance;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.simibubi.create.Create.REGISTRATE;

public class CTBlockEntityTypes {
    public static final BlockEntityEntry<FreezableMixerBlockEntity> mixer = REGISTRATE
            .blockEntity("freezable_mixer", FreezableMixerBlockEntity::new)
            .instance(() -> FreezableMixerInstance::new)
            .validBlocks(AllBlocks.MECHANICAL_MIXER)
            .renderer(() -> FreezableMixerRenderer::new)
            .register();
    public static final BlockEntityEntry<FreezablePressBlockEntity> press = REGISTRATE
            .blockEntity("freezable_press", FreezablePressBlockEntity::new)
            .instance(() -> FreezablePressInstance::new)
            .validBlocks(AllBlocks.MECHANICAL_PRESS)
            .renderer(() -> FreezablePressRenderer::new)
            .register();
    public static final BlockEntityEntry<CogCrankBlockEntity> cogCrank = REGISTRATE
            .blockEntity("cog_crank", CogCrankBlockEntity::new)
            .instance(() -> CogCrankInstance::new)
            .validBlocks(CTBlocks.cogCrank)
            .renderer(() -> CogCrankRenderer::new)
            .register();
    public static final BlockEntityEntry<BronzeTankBlockEntity> bronzeTank = REGISTRATE
            .blockEntity("bronze_tank", BronzeTankBlockEntity::new)
            .validBlocks(CTBlocks.bronzeTank)
            .renderer(() -> BronzeTankRenderer::new)
            .register();
    public static final BlockEntityEntry<BoilerTankBlockEntity> boilerTank = REGISTRATE
            .blockEntity("boiler_tank", BoilerTankBlockEntity::new)
            .validBlocks(CTBlocks.boilerTank)
            .renderer(() -> BoilerTankRenderer::new)
            .register();
    public static final BlockEntityEntry<FluidPipeBlockEntity> encasedSteamPipe = REGISTRATE
            .blockEntity("encased_steam_pipe", FluidPipeBlockEntity::new)
            .validBlocks(CTBlocks.encasedSteamPipe)
            .register();
    public static final BlockEntityEntry<FluidPipeBlockEntity> steamPipe = REGISTRATE
            .blockEntity("steam_pipe", FluidPipeBlockEntity::new)
            .validBlocks(CTBlocks.steamPipe)
            .register();
    public static final BlockEntityEntry<StraightPipeBlockEntity> glassSteamPipe = REGISTRATE
            .blockEntity("glass_steam_pipe", StraightPipeBlockEntity::new)
            .validBlocks(CTBlocks.glassSteamPipe)
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();
    public static final BlockEntityEntry<PumpBlockEntity> steamPump = REGISTRATE
            .blockEntity("steam_pump", PumpBlockEntity::new)
            .instance(() -> PumpCogInstance::new)
            .validBlocks(CTBlocks.steamPump)
            .renderer(() -> PumpRenderer::new)
            .register();
    //public static final BlockEntityEntry<KineticBlockEntity> freezedownEncasedShaft = REGISTRATE
    //        .blockEntity("freezedown_encased_shaft", KineticBlockEntity::new)
    //        .instance(() -> ShaftInstance::new, false)
    //        .validBlocks(CTBlocks.boilerEncasedShaft)
    //        .renderer(() -> ShaftRenderer::new)
    //        .register();
    
    public static void register() {}
}
