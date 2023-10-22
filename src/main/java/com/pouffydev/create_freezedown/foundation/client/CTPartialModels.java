package com.pouffydev.create_freezedown.foundation.client;

import com.jozufozu.flywheel.core.PartialModel;
import com.pouffydev.create_freezedown.Thermology;

public class CTPartialModels {
    public static final PartialModel
            HAND_CRANK_HANDLE = block("cog_crank/handle")
            ;
    private static PartialModel block(String path) {
        return new PartialModel(Thermology.asResource("block/" + path));
    }
    
    private static PartialModel entity(String path) {
        return new PartialModel(Thermology.asResource("entity/" + path));
    }
    
    public static void init() {
        // init static fields
    }
}
