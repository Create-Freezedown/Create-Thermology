package com.pouffydev.create_freezedown.foundation.climate.temperature;

import com.pouffydev.create_freezedown.foundation.climate.CTBodyDataSyncPacket;
import com.pouffydev.create_freezedown.foundation.climate.data.ChunkData;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.IHeatingEquipment;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.IWarmKeepingEquipment;
import com.pouffydev.create_freezedown.foundation.util.CTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;

import static com.pouffydev.create_freezedown.foundation.climate.CTPacketHandler.getChannel;


@Mod.EventBusSubscriber
public class TemperatureUpdate {
    public static final float HEAT_EXCHANGE_CONSTANT = 0.0012F;
    public static final float SELF_HEATING_CONSTANT = 0.036F;
    
    private static final class HeatingEquipment {
        IHeatingEquipment e;
        ItemStack i;
        
        public HeatingEquipment(IHeatingEquipment e, ItemStack i) {
            this.e = e;
            this.i = i;
        }
        
        public float compute(float body, float env) {
            return e.compute(i, body, env);
        }
    }
    
    /**
     * Perform temperature tick logic
     *
     * @param event fired every tick on player
     */
    @SubscribeEvent
    public static void updateTemperature(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START
                && event.player instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.player;
            if (player.tickCount % 10 != 0 || player.isCreative() || player.isSpectator())
                return;
            //soak in water modifier
            if (player.isInWater()) {
                boolean hasArmor = false;
                for (ItemStack is : player.getArmorSlots()) {
                    if (!is.isEmpty()) {
                        hasArmor = true;
                        break;
                    }
                }
                //MobEffectInstance current = player.getEffect(FHEffects.WET);
                //if (hasArmor)
                //    player.addEffect(new MobEffectInstance(FHEffects.WET, 400, 0));// punish for wet clothes
                //else if (current == null || current.getDuration() < 100)
                //    player.addEffect(new MobEffectInstance(FHEffects.WET, 100, 0));
            }
            //load current data
            float current = TemperatureCore.getBodyTemperature(player);
            if (current < 0) {
                //base it on the current difficulty
                float delt = SELF_HEATING_CONSTANT * (player.getLevel().getDifficulty().getId() + 1);
                player.causeFoodExhaustion(Math.min(delt,-current)*0.5f);//cost hunger for cold.
                current += delt;
            }
            //world and chunk temperature
            Level world = player.getLevel();
            BlockPos pos = new BlockPos(player.getX(),player.getEyeY(),player.getZ());
            float envtemp = ChunkData.getTemperature(world, pos);
            //time temperature
            float skyLight = world.getBrightness(LightLayer.SKY, pos);
            float gameTime = world.getDayTime() % 24000L;
            gameTime = gameTime / (200 / 3);
            gameTime = Mth.sin((float) Math.toRadians(gameTime));
            float bt=TemperatureCore.getBlockTemp(player);
            envtemp += bt;
            envtemp += skyLight > 5.0F ?
                    (world.isRaining() ?
                            (CTUtils.isRainingAt(player.getOnPos(), world)?-8F:-5f)
                            : (gameTime * 5.0F))
                    : -5F;
            // burning heat
            if (player.isOnFire())
                envtemp += 150F;
            // normalize
            envtemp -= 37F;
            float keepwarm = 0;
            //list of equipments to be calculated
            ArrayList<HeatingEquipment> equipments = new ArrayList<>(7);
            for (ItemStack is : player.getArmorSlots()) {
                if (is.isEmpty())
                    continue;
                Item it = is.getItem();
                equipments.add(new HeatingEquipment((IHeatingEquipment) it, is));
                keepwarm += ((IWarmKeepingEquipment) it).getFactor(player, is);
            }
            {//main hand
                ItemStack hand = player.getMainHandItem();
                Item it = hand.getItem();
                if (it instanceof IHeatingEquipment && ((IHeatingEquipment) it).canHandHeld())
                    equipments.add(new HeatingEquipment((IHeatingEquipment) it, hand));
            }
            {//off hand
                ItemStack hand = player.getOffhandItem();
                Item it = hand.getItem();
                if (it instanceof IHeatingEquipment && ((IHeatingEquipment) it).canHandHeld())
                    equipments.add(new HeatingEquipment((IHeatingEquipment) it, hand));
                ;
            }
            if (keepwarm > 1)//prevent negative
                keepwarm = 1;
            //environment heat exchange
            float dheat = HEAT_EXCHANGE_CONSTANT * (1 - keepwarm) * (envtemp - current);
            //simulate temperature transform to get heating device working
            float simulated = current / dheat;
            for (HeatingEquipment it : equipments) {
                float addi = it.compute(simulated, envtemp);
                dheat += addi;
                simulated += addi;
            }
            //if (dheat > 0.1)
            //    player.attack(FHDamageSources.HYPERTHERMIA_INSTANT, (dheat) * 10);
            //else if (dheat < -0.1)
            //    player.attack(FHDamageSources.HYPOTHERMIA_INSTANT, (-dheat) * 10);
            current += dheat;
            if (current < -10)
                current = -10;
            else if (current > 10)
                current = 10;
            float lenvtemp=TemperatureCore.getEnvTemperature(player);//get a smooth change in display
            TemperatureCore.setTemperature(player, current, (envtemp + 37)*.2f+lenvtemp*.8f);
            getChannel().send(PacketDistributor.PLAYER.with(() -> player), new CTBodyDataSyncPacket(player));
        }
    }
    
    /**
     * Perform temperature effect
     *
     * @param event fired every tick on player
     */
    @SubscribeEvent
    public static void regulateTemperature(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END
                && event.player instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.player;
            double calculatedTarget = TemperatureCore.getBodyTemperature(player);
            if (!(player.isCreative() || player.isSpectator())) {
                if (calculatedTarget > 1 || calculatedTarget < -1) {
                    //if (!player.hasEffect(FHEffects.HYPERTHERMIA) && !player.hasEffect(FHEffects.HYPOTHERMIA)) {
                        if (calculatedTarget > 1) { // too hot
                            if (calculatedTarget <= 2) {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPERTHERMIA, 100, 0));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            } else if (calculatedTarget <= 3) {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPERTHERMIA, 100, 1));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.CONFUSION, 100, 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            } else if (calculatedTarget <= 5) {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPERTHERMIA, 100, 2));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.CONFUSION, 100, 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            } else {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPERTHERMIA, 100, (int) (calculatedTarget - 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.CONFUSION, 100, 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            }
                        } else { // too cold
                            if (calculatedTarget >= -2) {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPOTHERMIA, 100, 0));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            } else if (calculatedTarget >= -3) {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPOTHERMIA, 100, 1));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.CONFUSION, 100, 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            } else if (calculatedTarget >= -5) {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPOTHERMIA, 100, 2));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.CONFUSION, 100, 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            } else {
                                //player.addEffect(new MobEffectInstance(FHEffects.HYPOTHERMIA, 100, (int) (-calculatedTarget - 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.CONFUSION, 100, 2)));
                                player.addEffect(CTUtils.noHeal(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0)));
                            }
                        }
                    //}
                }
            }
        }
    }
}
