package com.pouffydev.create_freezedown.foundation.creative;

import com.pouffydev.create_freezedown.CTItems;
import net.minecraft.world.item.ItemStack;

public class MainItemTabBase extends CTMainItemTab {
    public MainItemTabBase() {
        super("base");
    }
    
    @Override
    public ItemStack makeIcon() {
        return CTItems.thermalloy_ingot.asStack();
    }
}
