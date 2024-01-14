package com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces;

import net.minecraft.world.item.ItemStack;

public interface IHeatingEquipment {
    /**
     * Compute new body temperature.<br>
     *
     * @param stack           the stack<br>
     * @param bodyTemp        the body temp<br>
     * @param environmentTemp the environment temp<br>
     * @return returns body temperature change
     */
    float compute(ItemStack stack, float bodyTemp, float environmentTemp);
    
    /**
     * get max temperature delta.<br>
     *
     * @param stack the stack<br>
     * @return returns max temperature delta
     */
    float getMax(ItemStack stack);
    
    default boolean canHandHeld() {
        return false;
    }
}
