package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankBlockEntity;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankInstance;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankRenderer;
import com.pouffydev.create_freezedown.content.kinetics.freezable.mixer.FreezableMixerBlockEntity;
import com.pouffydev.create_freezedown.content.kinetics.freezable.mixer.FreezableMixerInstance;
import com.pouffydev.create_freezedown.content.kinetics.freezable.mixer.FreezableMixerRenderer;
import com.pouffydev.create_freezedown.content.kinetics.freezable.press.FreezablePressBlockEntity;
import com.pouffydev.create_freezedown.content.kinetics.freezable.press.FreezablePressInstance;
import com.pouffydev.create_freezedown.content.kinetics.freezable.press.FreezablePressRenderer;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import com.simibubi.create.content.kinetics.crank.HandCrankInstance;
import com.simibubi.create.content.kinetics.crank.HandCrankRenderer;
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
    public static void register() {}
}
