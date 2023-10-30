package com.pouffydev.create_freezedown.foundation;

import com.pouffydev.create_freezedown.Thermology;
import com.pouffydev.create_freezedown.foundation.climate.CTDataReloadManager;
import com.pouffydev.create_freezedown.foundation.climate.CTTemperatureDisplayPacket;
import com.pouffydev.create_freezedown.foundation.climate.data.*;
import com.pouffydev.create_freezedown.foundation.climate.temperature.TemperatureCore;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.ITempAdjustFood;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thermology.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CTCommon {
    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(CTDataReloadManager.INSTANCE);
    }
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ClimateData.register(event);
        ChunkDataCapabilityProvider.register(event);
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            Level world = event.level;
            if (!world.isClientSide && world instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) world;
                // Update clock source every second, and check hour data if it needs an update
                if (serverWorld.getGameTime() % 20 == 0) {
                    ClimateData data = ClimateData.get(serverWorld);
                    data.updateClock(serverWorld);
                    data.updateCache(serverWorld);
                    data.trimTempEventStream();
                }
                //if (world.getDayTime() % 24000 == 40) {
                //    for(Player spe:((ServerLevel) world).getPlayers((p)->true)) {
                //        if(spe instanceof ServerPlayer &&!(spe instanceof FakePlayer)) {
                //            ServerPlayer serverPlayer=(ServerPlayer) spe;
                //            long energy = EnergyCore.getEnergy(spe);
                //            if (energy > 10000)
                //                serverPlayer.sendStatusMessage(GuiUtils.translateMessage("energy.full"), false);
                //            else if (energy >= 5000)
                //                serverPlayer.sendStatusMessage(GuiUtils.translateMessage("energy.suit"), false);
                //            else
                //                serverPlayer.sendStatusMessage(GuiUtils.translateMessage("energy.lack"), false);
                //        }
                //    }
                //}
            }
            
        }
    }
    
    
    //@SubscribeEvent
    //public static void canUseBlock(PlayerInteractEvent.RightClickBlock event) {
    //    if (!ResearchListeners.canUseBlock(event.getEntity(), event.getLevel().getBlockState(event.getHitVec().getBlockPos()).getBlock())) {
    //        event.setUseBlock(Event.Result.DENY);
    //
    //        event.getEntity().sendSystemMessage(Lang.translate(Thermology.ID+ "research.cannot_use_block").component());
    //    }
    //
    //}
    
    @SubscribeEvent
    public static void beforeCropGrow(BlockEvent.CropGrowEvent.Pre event) {
        Block growBlock =event.getState().getBlock();
        
        float temp = ChunkData.getTemperature(event.getLevel(), event.getPos());
        
        if (growBlock instanceof GrassBlock) {
            return;
        } else {
            if (temp < WorldClimate.VANILLA_PLANT_GROW_TEMPERATURE) {
                // Set back to default state, might not be necessary
                if (temp<0&& event.getLevel().getRandom().nextInt(3) == 0) {
                    BlockState cbs=event.getLevel().getBlockState(event.getPos());
                    if(cbs.is(growBlock)&&cbs!= growBlock.defaultBlockState())
                        event.getLevel().setBlock(event.getPos(), growBlock.defaultBlockState(), 2);
                }
                event.setResult(Event.Result.DENY);
            }else if(temp>WorldClimate.VANILLA_PLANT_GROW_TEMPERATURE_MAX) {
                if(event.getLevel().getRandom().nextInt(3) == 0) {
                    BlockState cbs=event.getLevel().getBlockState(event.getPos());
                    if(cbs.is(growBlock))
                        event.getLevel().setBlock(event.getPos(), Blocks.AIR.defaultBlockState(), 2);
                }
                event.setResult(Event.Result.DENY);
            }
            
        }
    }
    @SubscribeEvent
    public static void onUseBoneMeal(BonemealEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            Block growBlock = event.getBlock().getBlock();
            float temp = ChunkData.getTemperature(event.getLevel(), event.getPos());
            /*if (growBlock instanceof FHCropBlock) {
                int growTemp = ((FHCropBlock) growBlock).getGrowTemperature()+WorldClimate.BONEMEAL_TEMPERATURE;
                if (temp < growTemp) {
                    event.setCanceled(true);
                    FHTemperatureDisplayPacket.sendStatus(player,
                            "crop_no_bonemeal",false,growTemp);
                }
            } else if (growBlock instanceof FHBerryBushBlock) {
                int growTemp = ((FHBerryBushBlock) growBlock).getGrowTemperature()+WorldClimate.BONEMEAL_TEMPERATURE;
                if (temp < growTemp) {
                    event.setCanceled(true);
                    FHTemperatureDisplayPacket.sendStatus(player,"crop_no_bonemeal",false, growTemp);
                }
            } else if (growBlock.is(IEBlocks.Misc.hempPlant)) {
                if (temp < WorldClimate.HEMP_GROW_TEMPERATURE+WorldClimate.BONEMEAL_TEMPERATURE) {
                    event.setCanceled(true);
                    CTTemperatureDisplayPacket.sendStatus(player,"crop_no_bonemeal",false,
                            WorldClimate.HEMP_GROW_TEMPERATURE+WorldClimate.BONEMEAL_TEMPERATURE);
                }
            } else*/ if (temp < WorldClimate.VANILLA_PLANT_GROW_TEMPERATURE+WorldClimate.BONEMEAL_TEMPERATURE) {
                event.setCanceled(true);
                CTTemperatureDisplayPacket.sendStatus(player,"crop_no_bonemeal",false,
                        WorldClimate.VANILLA_PLANT_GROW_TEMPERATURE+WorldClimate.BONEMEAL_TEMPERATURE);
            }
        }
    }
    
    @SubscribeEvent
    public static void finishedEatingFood(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() != null && !event.getEntity().level.isClientSide
                && event.getEntity() instanceof ServerPlayer) {
            ItemStack is = event.getItem();
            Item it = event.getItem().getItem();
            ITempAdjustFood adj = null;
            // System.out.println(it.getRegistryName());
            if (it instanceof ITempAdjustFood) {
                adj = (ITempAdjustFood) it;
            } else {
                adj = CTDataManager.getFood(is);
            }
            if (adj != null) {
                float current = TemperatureCore.getBodyTemperature((ServerPlayer) event.getEntity());
                float max = adj.getMaxTemp(event.getItem());
                float min = adj.getMinTemp(event.getItem());
                float heat = adj.getHeat(event.getItem(),TemperatureCore.getEnvTemperature((ServerPlayer) event.getEntity()));
                //if (heat > 1) {
                //    ((ServerPlayer) event.getEntity()).attack(FHDamageSources.HYPERTHERMIA_INSTANT, (heat) * 2);
                //} else if (heat < -1)
                //    ((ServerPlayer) event.getEntity()).attack(FHDamageSources.HYPOTHERMIA_INSTANT, (heat) * 2);
                if (heat > 0) {
                    if (current >= max)
                        return;
                    current += heat;
                    if (current > max)
                        current = max;
                } else {
                    if (current <= min)
                        return;
                    current += heat;
                    if (current <= min)
                        return;
                }
                TemperatureCore.setBodyTemperature((ServerPlayer) event.getEntity(), current);
            }
        }
    }
}
