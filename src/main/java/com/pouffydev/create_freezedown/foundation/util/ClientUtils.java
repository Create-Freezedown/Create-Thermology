package com.pouffydev.create_freezedown.foundation.util;

import com.simibubi.create.AllParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ClientUtils {
    public static float spgamma;
    public static boolean applyspg;
    
    public static Minecraft mc() {
        return Minecraft.getInstance();
    }
    
    public static Level getWorld() {
        return Minecraft.getInstance().level;
    }
    
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }
    
    public static void spawnSmokeParticles(Level worldIn, BlockPos pos) {
        Random random = (Random) worldIn.getRandom();
        worldIn.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, pos.getX() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + random.nextDouble() + random.nextDouble(), pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.05D, 0.0D);
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.002D, 0.01D, 0.0D);
    }
    
    public static void spawnSteamParticles(Level worldIn, BlockPos pos) {
        Random random = (Random) worldIn.getRandom();
        worldIn.addParticle((ParticleOptions) AllParticleTypes.STEAM_JET.get(), true, pos.getX() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.5D + random.nextDouble() + random.nextDouble(), pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.12D, 0.0D);
        worldIn.addParticle((ParticleOptions) AllParticleTypes.STEAM_JET.get(), pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.05D, 0.0D);
    }
    
    public static void spawnFireParticles(Level worldIn, BlockPos pos) {
        Random random = (Random) worldIn.getRandom();
        // Upward flame
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.01D, 0.0D);
        // Side flame (4 directions)
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.01D, 0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), -0.01D, 0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0D, 0.01D);
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0D, -0.01D);
    }
    
    public static void spawnT2FireParticles(Level worldIn, BlockPos pos) {
        Random random = (Random) worldIn.getRandom();
        // Upward flame
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.01D, 0.0D);
        // Side flame (4 directions)
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.01D, 0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), -0.01D, 0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0D, 0.01D);
        worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0D, -0.01D);
    }
}
