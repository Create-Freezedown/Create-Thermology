package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;

public class WorldTempData extends JsonDataHolder {
    
    public WorldTempData(JsonObject data) {
        super(data);
    }
    
    public float getTemp() {
        return this.getFloatOrDefault("temperature", 0F);
    }
}
