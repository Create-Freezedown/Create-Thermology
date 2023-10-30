package com.pouffydev.create_freezedown.foundation.climate.temperature.data;

import com.pouffydev.create_freezedown.foundation.climate.data.CTDataManager;
import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.ITempAdjustFood;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class CupTempAdjustProxy implements ITempAdjustFood {
    float efficiency;
    ITempAdjustFood defData;
    
    public CupTempAdjustProxy(float efficiency, ITempAdjustFood defaultData) {
        this.efficiency = efficiency;
        this.defData = defaultData;
    }
    
    @Override
    public float getMaxTemp(ItemStack is) {
        if (defData != null)
            return defData.getMaxTemp(is);
        return ITempAdjustFood.super.getMaxTemp(is);
    }
    
    @Override
    public float getMinTemp(ItemStack is) {
        if (defData != null)
            return defData.getMinTemp(is);
        return ITempAdjustFood.super.getMinTemp(is);
    }
    
    @Override
    public float getHeat(ItemStack is,float env) {
        LazyOptional<IFluidHandlerItem> ih = FluidUtil.getFluidHandler(is);
        if (ih.isPresent()) {
            IFluidHandlerItem f = ih.resolve().get();
            FluidStack fs = f.getFluidInTank(0);
            if (!fs.isEmpty()) {
                float dh= CTDataManager.getDrinkHeat(fs);
                if((env>37&&dh<0)||(env<37&&dh>0))
                    return dh * efficiency;
                if(env==37)
                    return dh;
                
                return dh * (2-efficiency);
            }
        }
        if (defData != null)
            return defData.getHeat(is,env);
        return 0;
    }
}
