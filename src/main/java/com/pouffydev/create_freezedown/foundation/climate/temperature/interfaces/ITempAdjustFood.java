package com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces;

import net.minecraft.world.item.ItemStack;

public interface ITempAdjustFood {
    /**
     * Get max temperature this item can get.
     *
     * @param is the stack<br>
     * @return max temp<br>
     */
    default float getMaxTemp(ItemStack is) {
        return 15;
    }
    
    ;
    
    /**
     * Get min temperature this item can get.
     *
     * @param is the stack<br>
     * @return max temp<br>
     */
    default float getMinTemp(ItemStack is) {
        return -15;
    }
    
    ;
    
    /**
     * Get delta temperature this item would give.
     *
     * @param is the is<br>
     * @return heat<br>
     */
    float getHeat(ItemStack is, float env);
}
