package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.google.gson.JsonObject;
import com.pouffydev.create_freezedown.foundation.climate.data.JsonDataHolder;

public class CupData extends JsonDataHolder {
    
    public CupData(JsonObject data) {
        super(data);
    }
    
    public Float getEfficiency() {
        return this.getFloat("efficiency");
    }
}
