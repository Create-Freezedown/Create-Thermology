package com.pouffydev.create_freezedown.foundation.climate.data;

import com.pouffydev.create_freezedown.foundation.climate.temperature.data.BiomeTempData;
import com.pouffydev.create_freezedown.foundation.util.BiomeUtils;
import com.pouffydev.create_freezedown.foundation.util.CTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class WorldClimate {
    /**
     * Baseline temperature for temperate period.
     */
    public static final float CALM_PERIOD_BASELINE = -10;
    
    /**
     * The temporary uprising peak temperature of a cold period.
     */
    public static final float COLD_PERIOD_PEAK = -5;
    
    /**
     * The peak temperature of a warm period.
     */
    public static final float WARM_PERIOD_PEAK = -2;
    
    /**
     * The bottom temperature of a cold period.
     */
    public static final float COLD_PERIOD_BOTTOM_T1 = -15;
    public static final float COLD_PERIOD_BOTTOM_T2 = -20;
    public static final float COLD_PERIOD_BOTTOM_T3 = -30;
    public static final float COLD_PERIOD_BOTTOM_T4 = -40;
    public static final float COLD_PERIOD_BOTTOM_T5 = -50;
    public static final float COLD_PERIOD_BOTTOM_T6 = -60;
    public static final float COLD_PERIOD_BOTTOM_T7 = -70;
    public static final float COLD_PERIOD_BOTTOM_T8 = -80;
    public static final float COLD_PERIOD_BOTTOM_T9 = -90;
    public static final float COLD_PERIOD_BOTTOM_T10 = -100;
    public static final float[] BOTTOMS=new float[] {
            COLD_PERIOD_BOTTOM_T1,
            COLD_PERIOD_BOTTOM_T2,
            COLD_PERIOD_BOTTOM_T3,
            COLD_PERIOD_BOTTOM_T4,
            COLD_PERIOD_BOTTOM_T5,
            COLD_PERIOD_BOTTOM_T6,
            COLD_PERIOD_BOTTOM_T7,
            COLD_PERIOD_BOTTOM_T8,
            COLD_PERIOD_BOTTOM_T9,
            COLD_PERIOD_BOTTOM_T10
    };
    public static final float CO2_FREEZE_TEMP = -78;
    public static final float O2_FREEZE_TEMP = -218;
    public static final float O2_LIQUID_TEMP = -182;
    public static final float N2_FREEZE_TEMP = -209;
    public static final float N2_LIQUID_TEMP = -195;
    
    /**
     * The temperature when snow can reach the ground.
     */
    public static final float SNOW_TEMPERATURE = -13;
    
    /**
     * The temperature when snow becomes blizzard.
     */
    public static final float BLIZZARD_TEMPERATURE = -18;
    
    /**
     * The temperature when vanilla plants can grow.
     */
    public static final float VANILLA_PLANT_GROW_TEMPERATURE = 15;
    public static final int BONEMEAL_TEMPERATURE = 5;
    
    /**
     * The temperature when hemp can grow.
     */
    public static final float HEMP_GROW_TEMPERATURE = 0;
    
    public static final float ANIMAL_ALIVE_TEMPERATURE = -1.5f;
    
    public static final float FEEDED_ANIMAL_ALIVE_TEMPERATURE = -15f;
    
    public static final float VANILLA_PLANT_GROW_TEMPERATURE_MAX = 50;
    
    public static Map<Object, Float> worldbuffer = new HashMap<>();
    public static Map<Biome, Float> biomebuffer = new HashMap<>();
    
    /**
     * Get World temperature for a specific world, affected by weather and so on
     *
     * @param w the world<br>
     * @return world temperature<br>
     */
    public static float getWorldTemperature(LevelReader w, BlockPos pos) {
        Biome b=w.getBiome(pos).get();
        Float temp =null;
        Level level = w instanceof Level ? (Level) w : null;
        if(b!=null)
            temp=biomebuffer.computeIfAbsent(b, CTDataManager::getBiomeTemp);
        float wt = 0;
        if (w instanceof Level) {
            wt = worldbuffer.computeIfAbsent(w, (k) -> {
                Float fw = CTDataManager.getWorldTemp((Level) w);
                if (fw == null) return 0F;
                return fw;
            });
            
            // Add dynamic temperature baseline
            wt += ClimateData.getTemp((Level) w);
        }
        
        if (temp != null)
            return wt + temp;
        return wt;
    }
    
    public static void clear() {
        worldbuffer.clear();
        biomebuffer.clear();
    }
}
