package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.IWarmKeepingEquipment;
import com.pouffydev.create_freezedown.foundation.util.CTUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ArmorTempData extends JsonDataHolder implements IWarmKeepingEquipment {
    
    public ArmorTempData(JsonObject data) {
        super(data);
    }
    
    @Override
    public float getFactor(ServerPlayer pe, ItemStack stack) {
        float base = this.getFloatOrDefault("factor", 0F);
        if (pe == null) return base;
        if (pe.isOnFire())
            base += this.getFloatOrDefault("fire", 0F);
        if (pe.isInWater())//does not apply twice
            base += this.getFloatOrDefault("water", 0F);
        if (CTUtils.isRainingAt(pe.getOnPos(), pe.level)) {
//            if (pe.getServerWorld().getBiome(pe.getPosition()).getPrecipitation() == Biome.RainType.SNOW)
            base += this.getFloatOrDefault("snow", 0F);
//            else
//                base += this.getFloatOrDefault("rain", 0F);
        }
        
        float min = this.getFloatOrDefault("min", 0F);
        if (base < min) {
            base = min;
        } else {
            float max = this.getFloatOrDefault("max", 1F);
            if (base > max)
                base = max;
            
        }
        return base;
    }
}
