package com.pouffydev.create_freezedown.content.fluids.steam;

import com.pouffydev.create_freezedown.foundation.client.CTLang;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IHeatEmittingBE {
    default boolean shouldEmmitHeat() {
        return false;
    }
    
    default void changeTemp(Level level, BlockPos pos, int radius, int temp) {
        if (!shouldEmmitHeat())
            return;
        //ChunkData.addSphericTempAdjust(level, pos, radius, temp);
    }
    
    private MutableComponent getTempString(int temp, boolean isPlayerSneaking) {
        if (isPlayerSneaking)
            return CTLang.toTemperatureIntStringFahrenheit(temp);
        else
            return CTLang.toTemperatureIntString(temp);
    }
    
    default boolean temperatureOutputTooltip(List<Component> tooltip, boolean isPlayerSneaking, int temp) {
        if (!shouldEmmitHeat())
            return false;
        
        
        
        CTLang.translate("gui.goggles.temp_container").forGoggles(tooltip);
        Lang.builder().add(getTempString(temp, isPlayerSneaking))
                .forGoggles(tooltip, 1);
        return true;
    }
}
