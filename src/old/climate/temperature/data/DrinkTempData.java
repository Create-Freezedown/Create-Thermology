package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;

public class DrinkTempData extends JsonDataHolder {
    
    public DrinkTempData(JsonObject data) {
        super(data);
    }
    
    public float getHeat() {
        return this.getFloatOrDefault("heat", 0F);
    }
}
