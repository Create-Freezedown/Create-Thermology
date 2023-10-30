package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.ITempAdjustFood;
import net.minecraft.world.item.ItemStack;

public class FoodTempData extends JsonDataHolder implements ITempAdjustFood {
    
    public FoodTempData(JsonObject data) {
        super(data);
    }
    
    @Override
    public float getMaxTemp(ItemStack is) {
        return this.getFloatOrDefault("max", 15F);
    }
    
    @Override
    public float getMinTemp(ItemStack is) {
        return this.getFloatOrDefault("min", -15F);
    }
    
    @Override
    public float getHeat(ItemStack is,float env) {
        return this.getFloatOrDefault("heat", 0F);
    }
}
