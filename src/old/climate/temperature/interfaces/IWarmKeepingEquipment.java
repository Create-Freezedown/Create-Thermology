package com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IWarmKeepingEquipment {
    float getFactor(@Nullable ServerPlayer pe, ItemStack stack);
}
