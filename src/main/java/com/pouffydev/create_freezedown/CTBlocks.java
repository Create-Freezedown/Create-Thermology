package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.content.block.ThermalloyBlock;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankBlock;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankItem;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankModel;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankBlock;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankItem;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankModel;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankBlock;
import com.pouffydev.create_freezedown.foundation.client.CTBlockStateGen;
import com.pouffydev.create_freezedown.foundation.creative.CTItemTab;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.tank.FluidTankGenerator;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.Tags;

import static com.pouffydev.create_freezedown.Thermology.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;

@SuppressWarnings({"removal", "LambdaBodyCanBeCodeBlock"})
public class CTBlocks {
    static {
        REGISTRATE.creativeModeTab(() -> CTItemTab.BASE_CREATIVE_TAB);
    }
    public static final BlockEntry<ThermalloyBlock> thermalloyBlock = REGISTRATE.block("thermalloy_block", ThermalloyBlock::new)
            .initialProperties(() -> Blocks.NETHERITE_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_BLUE))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(CTBlockStateGen.heatConditioned())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                    .define('T', AllTags.forgeItemTag("ingots/thermalloy"))
                    .pattern("TTT")
                    .pattern("TTT")
                    .pattern("TTT")
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(ctx.get()))
                    .save(prov))
            .transform(tagBlockAndItem("storage_blocks/thermalloy"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .model(AssetLookup.customBlockItemModel("thermalloy_block_normal"))
            .build()
            .lang("Block of Thermalloy")
            .register();
    public static final BlockEntry<CogCrankBlock> cogCrank = REGISTRATE.block("cog_crank", CogCrankBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.color(MaterialColor.PODZOL))
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .transform(BlockStressDefaults.setCapacity(8.0))
            .transform(BlockStressDefaults.setGeneratorSpeed(CogCrankBlock::getSpeedRange))
            .tag(AllTags.AllBlockTags.BRITTLE.tag)
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 1)
                    .requires(AllBlocks.HAND_CRANK.get())
                    .requires(AllBlocks.COGWHEEL.get())
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(ctx.get()))
                    .save(prov))
            .onRegister(ItemUseOverrides::addBlock)
            .item()
            .transform(customItemModel())
            .register();
    
    public static final BlockEntry<BronzeTankBlock> bronzeTank = REGISTRATE.block("bronze_tank", BronzeTankBlock::regular)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new FluidTankGenerator()::generate)
            .onRegister(CreateRegistrate.blockModel(() -> BronzeTankModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(BronzeTankItem::new)
            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
            .build()
            .register();
    public static final BlockEntry<BoilerTankBlock> boilerTank = REGISTRATE.block("boiler_tank", BoilerTankBlock::regular)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new FluidTankGenerator()::generate)
            .onRegister(CreateRegistrate.blockModel(() -> BoilerTankModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(BoilerTankItem::new)
            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
            .build()
            .register();
    public static void register() {}
}
