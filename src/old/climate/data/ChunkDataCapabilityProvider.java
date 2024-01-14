package com.pouffydev.create_freezedown.foundation.climate.data;

import com.pouffydev.create_freezedown.Thermology;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ChunkDataCapabilityProvider {
    public static Capability<ChunkData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final ResourceLocation KEY = new ResourceLocation(Thermology.ID, "chunk_data");
    
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(ChunkData.class);
    }
}
