package com.pouffydev.create_freezedown.foundation.climate.temperature;

import com.pouffydev.create_freezedown.Thermology;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class TemperatureCore {
    public static float getBlockTemp(ServerPlayer spe) {
		/*long time = System.nanoTime();
		try {*/
        
        return new TemperatureSimulator(spe).getBlockTemperature(spe.getX(), spe.getEyeY(), spe.getZ());

		/*} finally {
			long delta = System.nanoTime() - time;
			System.out.println(String.format("total cost %.3f ms", (delta / 1000000f)));
		}*/
    
    }
    
    public static final String DATA_ID = Thermology.ID + ":data";
    
    /**
     * On the basis of 37 celsius degree.
     * Example: return -1 when body temp is 36C.
     */
    public static float getBodyTemperature(Player spe) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            return 0;
        return nc.getFloat("bodytemperature");
    }
    
    public static float getLastTemperature(Player spe) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            return 0;
        return nc.getFloat("lasttemperature");
    }
    
    /**
     * On the basis of 0 celsius degree.
     * Example: return -20 when env temp is -20C.
     */
    public static float getEnvTemperature(Player spe) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            return 0;
        return nc.getFloat("envtemperature");
    }
    
    public static CompoundTag getFHData(Player spe) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            return new CompoundTag();
        return nc;
    }
    
    public static void setFHData(Player spe, CompoundTag nc) {
        spe.getPersistentData().put(DATA_ID, nc);
    }
    
    public static void setBodyTemperature(Player spe, float val) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            nc = new CompoundTag();
        nc.putFloat("bodytemperature", val);
        spe.getPersistentData().put(DATA_ID, nc);
    }
    
    public static void setEnvTemperature(Player spe, float val) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            nc = new CompoundTag();
        nc.putFloat("envtemperature", val);
        spe.getPersistentData().put(DATA_ID, nc);
    }
    
    public static void setTemperature(Player spe, float body, float env) {
        CompoundTag nc = spe.getPersistentData().getCompound(DATA_ID);
        if (nc == null)
            nc = new CompoundTag();
        nc.putFloat("bodytemperature", body);
        nc.putFloat("envtemperature", env);
        nc.putFloat("deltatemperature", nc.getFloat("lasttemperature") - body);
        nc.putFloat("lasttemperature", body);
        spe.getPersistentData().put(DATA_ID, nc);
    }
}
