package com.pouffydev.create_freezedown.foundation.climate.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pouffydev.create_freezedown.foundation.climate.temperature.data.*;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.ITempAdjustFood;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.IWarmKeepingEquipment;
import com.pouffydev.create_freezedown.foundation.util.BiomeUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class CTDataManager {
    public enum CTDataType {
        Armor(new DataType<>(ArmorTempData.class, "temperature", "armor")),
        Biome(new DataType<>(BiomeTempData.class, "temperature", "biome")),
        Food(new DataType<>(FoodTempData.class, "temperature", "food")),
        Block(new DataType<>(BlockTempData.class, "temperature", "block")),
        Drink(new DataType<>(DrinkTempData.class, "temperature", "drink")),
        Cup(new DataType<>(CupData.class, "temperature", "cup")),
        World(new DataType<>(WorldTempData.class, "temperature", "world"));
        
        public static class DataType<T extends JsonDataHolder> {
            final Class<T> dataCls;
            final String location;
            public final String domain;
            
            public DataType(Class<T> dataCls, String domain, String location) {
                this.location = location;
                this.dataCls = dataCls;
                this.domain = domain;
            }
            
            public T create(JsonObject jo) {
                try {
                    return dataCls.getConstructor(JsonObject.class).newInstance(jo);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                         | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    // TODO Auto-generated catch block
                    throw new RuntimeException(e);
                }
            }
            
            public String getLocation() {
                if (location.endsWith(".json"))
                    return domain + "/" + location;
                return domain + "/" + location + ".json";
            }
            public Predicate<ResourceLocation> endsWith(String s) {
                return rl -> rl.getPath().endsWith(s);
            }
        }
        
        public final DataType<? extends JsonDataHolder> type;
        
        private CTDataType(DataType<? extends JsonDataHolder> type) {
            this.type = type;
        }
        
    }
    
    public static class ResourceMap<T extends JsonDataHolder> extends HashMap<ResourceLocation, T> {
        /**
         *
         */
        private static final long serialVersionUID = 1564047056157250446L;
        
        public ResourceMap() {
            super();
        }
        
        public ResourceMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
        
        public ResourceMap(int initialCapacity) {
            super(initialCapacity);
        }
        
        public ResourceMap(Map<? extends ResourceLocation, ? extends T> m) {
            super(m);
        }
    }
    
    private CTDataManager() {
    }
    
    @SuppressWarnings("rawtypes")
    public static final EnumMap<CTDataType, ResourceMap> ALL_DATA = new EnumMap<>(CTDataType.class);
    public static boolean synched = false;
    private static final JsonParser parser = new JsonParser();
    
    static {
        for (CTDataType dt : CTDataType.values()) {
            ALL_DATA.put(dt, new ResourceMap<>());
        }
    }
    
    public static final void reset() {
        synched = false;
        for (ResourceMap<?> rm : ALL_DATA.values())
            rm.clear();
    }
    
    @SuppressWarnings("unchecked")
    public static final void register(CTDataType dt, JsonObject data) {
        JsonDataHolder jdh = dt.type.create(data);
        //System.out.println("registering "+dt.type.location+": "+jdh.getId());
        ALL_DATA.get(dt).put(jdh.getId(), jdh);
        synched = false;
    }
    
    @SuppressWarnings("unchecked")
    public static final <T extends JsonDataHolder> ResourceMap<T> get(CTDataType dt) {
        return ALL_DATA.get(dt);
        
    }
    
    @SuppressWarnings("unchecked")
    public static final void load(DataEntry[] entries) {
        reset();
        for (DataEntry de : entries) {
            JsonDataHolder jdh = de.type.type.create(parser.parse(de.data).getAsJsonObject());
            //System.out.println("registering "+dt.type.location+": "+jdh.getId());
            ALL_DATA.get(de.type).put(jdh.getId(), jdh);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static final DataEntry[] save() {
        int tsize = 0;
        for (ResourceMap map : ALL_DATA.values()) {
            tsize += map.size();
        }
        DataEntry[] entries = new DataEntry[tsize];
        int i = -1;
        for (Entry<CTDataType, ResourceMap> entry : ALL_DATA.entrySet()) {
            for (Object jdh : entry.getValue().values()) {
                entries[++i] = new DataEntry(entry.getKey(), ((JsonDataHolder) jdh).getData());
            }
        }
        return entries;
    }
    
    public static ITempAdjustFood getFood(ItemStack is) {
        CupData data = CTDataManager.<CupData>get(CTDataType.Cup).get(is.getItem().getDescriptionId());
        ResourceMap<FoodTempData> foodData = CTDataManager.get(CTDataType.Food);
        if (data != null) {
            return new CupTempAdjustProxy(data.getEfficiency(), foodData.get(is.getItem().getDescriptionId()));
        }
        return foodData.get(is.getItem().getDescriptionId());
    }
    
    public static IWarmKeepingEquipment getArmor(ItemStack is) {
        //System.out.println(is.getItem().getRegistryName());
        return CTDataManager.<ArmorTempData>get(CTDataType.Armor).get(is.getItem());
    }
    
    public static IWarmKeepingEquipment getArmor(String is) {
        //System.out.println(is.getItem().getRegistryName());
        return CTDataManager.<ArmorTempData>get(CTDataType.Armor).get(new ResourceLocation(is));
    }
    
    public static Float getBiomeTemp(Biome b) {
        if (b == null) return 0f;
        //Get the level
        BiomeTempData data = CTDataManager.<BiomeTempData>get(CTDataType.Biome).get(b);
        
        if (data != null)
            return data.getTemp();
        
        return 0F;
    }
    public static String getBiomeID(Biome b) {
        // just get the biome id
        return b.toString();
        
    }
    
    public static Float getWorldTemp(Level w) {
        WorldTempData data = CTDataManager.<WorldTempData>get(CTDataType.World).get(w.dimension().registry().getNamespace());
        if (data != null)
            return data.getTemp();
        return null;
    }
    
    public static BlockTempData getBlockData(Block b) {
        return CTDataManager.<BlockTempData>get(CTDataType.Block).get(b.getDescriptionId());
    }
    
    public static BlockTempData getBlockData(ItemStack b) {
        return CTDataManager.<BlockTempData>get(CTDataType.Block).get(b.getItem().getDescriptionId());
    }
    
    public static float getDrinkHeat(FluidStack f) {
        DrinkTempData dtd = CTDataManager.<DrinkTempData>get(CTDataType.Drink).get(f.getFluid().getFluidType().getDescriptionId());
        if (dtd != null)
            return dtd.getHeat();
        return -0.3f;
    }
}
