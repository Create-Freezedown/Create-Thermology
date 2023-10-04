package com.pouffydev.create_freezedown.content.block.util;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.util.StringRepresentable;

public enum BlockTemperature implements StringRepresentable {
    FREEZING,
    COLD,
    NORMAL,
    WARM,
    HOT,
    SCALDING;
    
    public static BlockTemperature byIndex(int index) {
        return values()[index];
    }
    
    public BlockTemperature nextActiveLevel() {
        return byIndex(ordinal() % (values().length - 1) + 1);
    }
    public static BlockTemperature findByName(String name) {
        for (BlockTemperature heatLevel : values()) {
            if (heatLevel.name().equals(name)) {
                return heatLevel;
            }
        }
        return null;
    }
    
    public boolean isAtLeast(BlockTemperature heatLevel) {
        return this.ordinal() >= heatLevel.ordinal();
    }
    
    @Override
    public String getSerializedName() {
        return Lang.asId(name());
    }
}
