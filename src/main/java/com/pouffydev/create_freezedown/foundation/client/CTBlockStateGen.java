package com.pouffydev.create_freezedown.foundation.client;

import com.pouffydev.create_freezedown.content.block.ThermalloyBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import java.util.Objects;

public class CTBlockStateGen {
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simpleCubeAll(
            String path) {
        return (c, p) -> p.simpleBlock(c.get(), p.models()
                .cubeAll(c.getName(), p.modLoc("block/" + path)));
    }
    public static <T extends Block> void cubeAll(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, String textureSubDir) {
        cubeAll(ctx, prov, textureSubDir, ctx.getName());
    }
    public static <T extends Block> void cubeAll(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                 String textureSubDir, String name) {
        String texturePath = "block/" + textureSubDir + name;
        prov.simpleBlock(ctx.get(), prov.models()
                .cubeAll(ctx.getName(), prov.modLoc(texturePath)));
    }
    public static <B extends ThermalloyBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> heatConditioned() {
        return (c, p) -> {
            p.getVariantBuilder(c.get()).forAllStatesExcept(state -> {
                String level = state.getValue(ThermalloyBlock.heatLevel).getSerializedName();
                return ConfiguredModel.builder().modelFile(
                    p.models().cubeAll(c.getName() + "_" + level,
                    p.modLoc("block/" + c.getName() + "_" + level)))
                    .build();
            });
        };
    }
    
}
