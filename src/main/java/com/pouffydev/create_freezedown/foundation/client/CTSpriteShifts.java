package com.pouffydev.create_freezedown.foundation.client;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

import static com.pouffydev.create_freezedown.Thermology.asResource;

public class CTSpriteShifts {
    public static final CTSpriteShiftEntry
            FLUID_TANK = getCT(AllCTTypes.RECTANGLE, "fluid_tank"),
            FLUID_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "fluid_tank_top"),
            FLUID_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "fluid_tank_inner"),
            BOILER_TANK = getCT(AllCTTypes.RECTANGLE, "boiler_tank"),
            BOILER_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "boiler_tank_top"),
            BOILER_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "boiler_tank_inner");
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, asResource("block/" + blockTextureName),
                asResource("block/" + connectedTextureName + "_connected"));
    }
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
    
    public static void init(){}
}
