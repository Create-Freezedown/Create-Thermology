package com.pouffydev.create_freezedown.content.research.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pouffydev.create_freezedown.content.research.ResearchUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemListeners {
    
    public static class ItemUseListener extends SimpleJsonResourceReloadListener {
        public static Map<ResourceLocation, Item> items = new HashMap<>();
        public ItemUseListener() {
            super(ResearchUtils.GSON, "research/item_use");
        }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            items.clear();
            
            for(Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
                ResourceLocation resourceLocation = entry.getKey();
                JsonObject json = entry.getValue().getAsJsonObject();
                if (json.has("blocks") || json.has("entities")) {
                    ResearchUtils.logDataError("items");
                }
                try {
                    NonNullList<Item> item = ResearchUtils.deserializeItemList(json);
                    item.forEach(i -> items.put(resourceLocation, i));
                } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                    ResearchUtils.LOGGER.error("Parsing error loading research, {}", resourceLocation, jsonParseException);
                }
            }
        }
    }
    
    public static class ItemSwingListener extends SimpleJsonResourceReloadListener {
        public static Map<ResourceLocation, Item> items = new HashMap<>();
        public ItemSwingListener() {
            super(ResearchUtils.GSON, "research/item_swing");
        }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            items.clear();
            
            for(Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
                ResourceLocation resourceLocation = entry.getKey();
                JsonObject json = entry.getValue().getAsJsonObject();
                if (json.has("blocks") || json.has("entities")) {
                    ResearchUtils.logDataError("items");
                }
                try {
                    NonNullList<Item> item = ResearchUtils.deserializeItemList(json);
                    item.forEach(i -> items.put(resourceLocation, i));
                } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                    ResearchUtils.LOGGER.error("Parsing error loading research, {}", resourceLocation, jsonParseException);
                }
            }
        }
    }
    
    public static class BlockPlaceListener extends SimpleJsonResourceReloadListener {
        public static Map<ResourceLocation, Item> items = new HashMap<>();
        public BlockPlaceListener() {
            super(ResearchUtils.GSON, "research/block_place");
        }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            items.clear();
            
            for(Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
                ResourceLocation resourceLocation = entry.getKey();
                JsonObject json = entry.getValue().getAsJsonObject();
                if (json.has("blocks") || json.has("entities")) {
                    ResearchUtils.logDataError("items");
                }
                try {
                    NonNullList<Item> item = ResearchUtils.deserializeItemList(json);
                    item.forEach(i -> items.put(resourceLocation, i));
                    if (!ResearchUtils.checkItemInstanceOfBlockItem(item)) {
                        ResearchUtils.LOGGER.error("Item is not an instance of BlockItem");
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                    ResearchUtils.LOGGER.error("Parsing error loading research, {}", resourceLocation, jsonParseException);
                }
            }
        }
    }
}
