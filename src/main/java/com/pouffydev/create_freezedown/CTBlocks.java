package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.content.block.ThermalloyBlock;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankBlock;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankItem;
import com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank.BoilerTankModel;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankBlock;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankItem;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankModel;
import com.pouffydev.create_freezedown.content.fluids.steam.pipe.*;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankBlock;
import com.pouffydev.create_freezedown.foundation.client.CTBlockStateGen;
import com.pouffydev.create_freezedown.foundation.client.CTSpriteShifts;
import com.pouffydev.create_freezedown.foundation.creative.CTItemTab;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.tank.FluidTankGenerator;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
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
    public static final BlockEntry<SteamPipeBlock> steamPipe = REGISTRATE.block("steam_pipe", SteamPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> SteamPipeAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<CasingBlock> boilerCasing = REGISTRATE.block("boiler_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .transform(BuilderTransformers.casing(() -> CTSpriteShifts.boilerCasing))
            .register();
    //public static final BlockEntry<EncasedShaftBlock> boilerEncasedShaft =
    //        REGISTRATE.block("boiler_encased_shaft", p -> new EncasedShaftBlock(p, boilerCasing::get))
    //                .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
    //                .transform(BuilderTransformers.encasedShaft("boiler", () -> CTSpriteShifts.boilerCasing))
    //                .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
    //                .transform(axeOrPickaxe())
    //                .register();
    public static final BlockEntry<EncasedSteamPipeBlock> encasedSteamPipe = REGISTRATE.block("encased_steam_pipe", p -> new EncasedSteamPipeBlock(p, boilerCasing::get))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.encasedPipe())
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(CTSpriteShifts.boilerCasing)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, CTSpriteShifts.boilerCasing,
                    (s, f) -> !s.getValue(EncasedSteamPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
            .onRegister(CreateRegistrate.blockModel(() -> SteamPipeAttachmentModel::new))
            .loot((p, b) -> p.dropOther(b, steamPipe.get()))
            .transform(EncasingRegistry.addVariantTo(steamPipe))
            .register();
    
    public static final BlockEntry<GlassSteamPipeBlock> glassSteamPipe = REGISTRATE.block("glass_steam_pipe", GlassSteamPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/steam_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> SteamPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, steamPipe.get()))
                    .register();
    
    public static final BlockEntry<SteamPumpBlock> steamPump = REGISTRATE.block("mechanical_steam_pump", SteamPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> SteamPipeAttachmentModel::new))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();
    
    public static void register() {}
}
