package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;

public class BlockTempData extends JsonDataHolder {
    public BlockTempData(JsonObject data) {
        super(data);
    }
    
    public float getTemp() {
        return this.getFloatOrDefault("temperature", 0.0F);
    }
    
    public int getRange() {
        return this.getIntOrDefault("range", 5);
    }
    
    public boolean isLevel() {
        return this.getBooleanOrDefault("level_divide", false);
    }
    
    public boolean isLit() {
        return this.getBooleanOrDefault("must_lit", false);
    }
}
