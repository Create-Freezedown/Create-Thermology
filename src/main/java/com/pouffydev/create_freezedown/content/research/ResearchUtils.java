package com.pouffydev.create_freezedown.content.research;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.pouffydev.create_freezedown.Thermology;
import com.pouffydev.create_freezedown.content.research.listener.ItemListeners;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Objects;

public class ResearchUtils {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = (new GsonBuilder()).create();
    public static String getResearchType(String type) {
        for (ResearchTypes researchType : ResearchTypes.values()) {
            if (researchType.getType().equals(type)) {
                return researchType.getType();
            }
        }
        return null;
    }
    public static void logDataError(String type) {
        String blocks = "Blocks";
        String items = "Items";
        String entities = "Entities";
        if (type.equals(items.toLowerCase())) {
            Thermology.LOGGER.error("Research type %s does not support %s or %s.".formatted(type, entities, blocks));
            throw new IllegalArgumentException();
        } else if (type.equals(entities.toLowerCase())) {
            Thermology.LOGGER.error("Research type %s does not support %s or %s.".formatted(type, blocks, items));
            throw new IllegalArgumentException();
        } else if (type.equals(blocks.toLowerCase())) {
            Thermology.LOGGER.error("Research type %s does not support %s or %s.".formatted(type, items, entities));
            throw new IllegalArgumentException();
        }
    }
    
    public static boolean checkItemInstanceOfBlockItem(NonNullList<Item> items) {
        for (Item item : items) {
            if (!(item instanceof ItemNameBlockItem)) {
                return false;
            }
        }
        return true;
    }
    public static EntityType<?> deserializeEntity(ResourceLocation name) {
        EntityType<?> entity = ForgeRegistries.ENTITY_TYPES.getValue(name);
        if (entity == null) {
            Thermology.LOGGER.error("Entity " + name + " does not exist.");
            throw new IllegalArgumentException();
        }
        return entity;
    }
    public static Item deserializeItem(ResourceLocation name) {
        Item item = ForgeRegistries.ITEMS.getValue(name);
        if (item == null) {
            Thermology.LOGGER.error("Item " + name + " does not exist.");
            throw new IllegalArgumentException();
        }
        return item;
    }
    public static Block deserializeBlock(ResourceLocation name) {
        Block block = ForgeRegistries.BLOCKS.getValue(name);
        if (block == null) {
            Thermology.LOGGER.error("Block " + name + " does not exist.");
            throw new IllegalArgumentException();
        }
        return block;
    }
    
    public static NonNullList<ItemStack> deserializeItemStackList(JsonObject jsonObject) {
        NonNullList<ItemStack> itemStackList = NonNullList.create();
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject input = jsonArray.get(i).getAsJsonObject();
            ItemStack stack = CraftingHelper.getItemStack(input, true);
            itemStackList.add(stack);
        }
        return itemStackList;
    }
    public static NonNullList<Item> deserializeItemList(JsonObject jsonObject) {
        NonNullList<Item> itemList = NonNullList.create();
        deserializeItemStackList(jsonObject).forEach(itemStack -> itemList.add(itemStack.getItem()));
        return itemList;
    }
    public static NonNullList<Block> deserializeBlockList(JsonObject jsonObject) {
        NonNullList<Block> blockList = NonNullList.create();
        JsonArray jsonArray = jsonObject.getAsJsonArray("blocks");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject input = jsonArray.get(i).getAsJsonObject();
            Block block = deserializeBlock(new ResourceLocation(input.get("block").getAsString()));
            blockList.add(block);
        }
        return blockList;
    }
    public static NonNullList<EntityType<?>> deserializeEntityList(JsonObject jsonObject) {
        NonNullList<EntityType<?>> entityList = NonNullList.create();
        JsonArray jsonArray = jsonObject.getAsJsonArray("entities");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject input = jsonArray.get(i).getAsJsonObject();
            EntityType<?> entity = deserializeEntity(new ResourceLocation(input.get("entity").getAsString()));
            entityList.add(entity);
        }
        return entityList;
    }
    public static String compileItemListToString(NonNullList<Item> items) {
        StringBuilder builder = new StringBuilder();
        for (Item item : items) {
            builder.append(item.getDescriptionId()).append(",");
        }
        return builder.toString();
    }
    public static String compileEntityListToString(NonNullList<EntityType<?>> entityTypes) {
        StringBuilder builder = new StringBuilder();
        for (EntityType<?> entityType : entityTypes) {
            builder.append(entityType.getDescriptionId()).append(",");
        }
        return builder.toString();
    }
    public static String compileBlockListToString(NonNullList<Block> blocks) {
        StringBuilder builder = new StringBuilder();
        for (Block block : blocks) {
            builder.append(block.getDescriptionId()).append(",");
        }
        return builder.toString();
    }
    private static boolean isCustomDataNameValid(JsonObject json) {
        if (json.has("customDataName")) {
            return !json.get("customDataName").getAsString().matches("[a-zA-Z]+");
        }
        return true;
    }
    public static boolean canItemBeResearched(Item toCheck, ResearchTypes researchType) {
        if (Objects.equals(researchType.getType(), "item_use")) {
            for (Item item : ItemListeners.ItemUseListener.items.values()) {
                if (item == toCheck) {
                    return true;
                }
            }
        } else if (Objects.equals(researchType.getType(), "item_swing")) {
            for (Item item : ItemListeners.ItemSwingListener.items.values()) {
                if (item == toCheck) {
                    return true;
                }
            }
        } else if (Objects.equals(researchType.getType(), "block_place")) {
            for (Item block : ItemListeners.BlockPlaceListener.items.values()) {
                if (block == toCheck) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isItemResearched(Item toCheck, Player player) {
        CompoundTag tag = player.getPersistentData();
        CompoundTag researchTag = tag.getCompound("unlocked_research");
        CompoundTag itemUseTag = researchTag.getCompound("item_use");
        CompoundTag itemSwingTag = researchTag.getCompound("item_swing");
        CompoundTag blockPlaceTag = researchTag.getCompound("block_place");
        
        if (tag.contains("unlocked_research")) {
            for (Item item : ItemListeners.ItemUseListener.items.values()) {
                if (item == toCheck && itemUseTag.contains(item.getDescriptionId())) {
                    return true;
                } else if (item == toCheck && itemSwingTag.contains(item.getDescriptionId())) {
                    return true;
                } else if (item == toCheck && blockPlaceTag.contains(item.getDescriptionId())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isItemResearched(Item toCheck, Player player, ResearchTypes researchType) {
        CompoundTag tag = player.getPersistentData();
        CompoundTag researchTag = tag.getCompound("unlocked_research");
        CompoundTag itemUseTag = researchTag.getCompound("item_use");
        CompoundTag itemSwingTag = researchTag.getCompound("item_swing");
        CompoundTag blockPlaceTag = researchTag.getCompound("block_place");
        if (tag.contains("unlocked_research")) {
            if (researchTag.contains("item_use")) {
                for (Item item : ItemListeners.ItemUseListener.items.values()) {
                    if (item == toCheck && itemUseTag.contains(item.getDescriptionId())) {
                        return true;
                    }
                }
            } else if (researchTag.contains("item_swing")) {
                for (Item item : ItemListeners.ItemSwingListener.items.values()) {
                    if (item == toCheck && itemSwingTag.contains(item.getDescriptionId())) {
                        return true;
                    }
                }
            } else if (researchTag.contains("block_place")) {
                for (Item block : ItemListeners.BlockPlaceListener.items.values()) {
                    if (block == toCheck && blockPlaceTag.contains(block.getDescriptionId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
