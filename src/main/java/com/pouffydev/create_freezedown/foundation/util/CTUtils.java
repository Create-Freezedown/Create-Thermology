package com.pouffydev.create_freezedown.foundation.util;

import com.google.common.collect.ImmutableList;
import com.pouffydev.create_freezedown.foundation.climate.data.ChunkData;
import com.pouffydev.create_freezedown.foundation.climate.data.WorldClimate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.ToIntFunction;

public class CTUtils {
    public static void giveItem(Player pe, ItemStack is) {
        if (!pe.addItem(is))
            pe.level.addFreshEntity(new ItemEntity(pe.level, pe.getX(), pe.getY(), pe.getZ(), is));
    }
    
    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
        };
    }
    public static boolean isRainingAt(BlockPos pos, Level world) {
        if (!world.isRaining()) {
            return false;
        } else if (!world.canSeeSky(pos)) {
            return false;
        } else if (world.getHeight() > pos.getY()) {
            return false;
        } else {
            return true;
        }
    }
    
    public static int getEnchantmentLevel(Enchantment enchID, CompoundTag tags) {
        ResourceLocation resourcelocation = Registry.ENCHANTMENT.getKey(enchID);
        ListTag listnbt = tags.getList("Enchantments", 10);
        
        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            ResourceLocation resourcelocation1 = ResourceLocation.tryParse(compoundnbt.getString("id"));
            if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
                return Mth.clamp(compoundnbt.getInt("lvl"), 0, 255);
            }
        }
        
        return 0;
    }
    
    public static MobEffectInstance noHeal(MobEffectInstance ei) {
        ei.setCurativeItems(ImmutableList.of());
        return ei;
    }
    
    public static boolean canGrassSurvive(LevelReader world, BlockPos pos) {
        float t = ChunkData.getTemperature(world, pos);
        return t >= WorldClimate.HEMP_GROW_TEMPERATURE && t <= WorldClimate.VANILLA_PLANT_GROW_TEMPERATURE_MAX;
    }
}
