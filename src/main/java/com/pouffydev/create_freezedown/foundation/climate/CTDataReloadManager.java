package com.pouffydev.create_freezedown.foundation.climate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pouffydev.create_freezedown.foundation.climate.data.CTDataManager;
import com.pouffydev.create_freezedown.foundation.climate.data.WorldClimate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

public class CTDataReloadManager implements ResourceManagerReloadListener {
    public static final CTDataReloadManager INSTANCE = new CTDataReloadManager();
    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        CTDataManager.reset();
        //StructureUtils.addBanedBlocks();
        WorldClimate.clear();
        for (CTDataManager.CTDataType dat : CTDataManager.CTDataType.values()) {
            for (Map.Entry<ResourceLocation, Resource> entry : manager.listResources(dat.type.getLocation(), dat.type.endsWith(".json")).entrySet()) {
                ResourceLocation rl = entry.getKey();
                Resource resource = entry.getValue();
                try {
                    Optional<Resource> res = manager.getResource(rl);
                    InputStream is = (InputStream) res.stream();
                    JsonObject jo = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
                    CTDataManager.register(dat, jo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
