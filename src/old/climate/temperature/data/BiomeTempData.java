package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;

public class BiomeTempData extends JsonDataHolder {
    
    public BiomeTempData(JsonObject data) {
        super(data);
    }
    
    public Float getTemp() {
        return this.getFloat("temperature");
    }
}
