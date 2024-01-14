package com.pouffydev.create_freezedown.foundation.climate.data;

import net.minecraft.CrashReportDetail;
import net.minecraft.world.level.ChunkPos;

public class ClimateCrash implements CrashReportDetail<String> {
    public static ChunkPos Last;
    
    @Override
    public String call() throws Exception {
        return "last calculating climate chunk: " + Last;
    }
}
