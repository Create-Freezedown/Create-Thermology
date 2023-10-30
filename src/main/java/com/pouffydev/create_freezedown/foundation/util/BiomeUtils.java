package com.pouffydev.create_freezedown.foundation.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class BiomeUtils {
    public static Optional<? extends Registry<Biome>> getBiomeRegistry(Level level) {
        return level.registryAccess().registry(ForgeRegistries.Keys.BIOMES);
    }
    
    public static Optional<ResourceLocation> getKeyForBiome(Level level, Biome biome) {
        return getBiomeRegistry(level).isPresent() ? Optional.of(getBiomeRegistry(level).get().getKey(biome)) : Optional.empty();
    }
    
    public static Optional<Biome> getBiomeForKey(Level level, ResourceLocation key) {
        return getBiomeRegistry(level).isPresent() ? getBiomeRegistry(level).get().getOptional(key) : Optional.empty();
    }
}
