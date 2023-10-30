package com.pouffydev.create_freezedown.foundation.climate.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class SphericTemperatureAdjust extends CubicTemperatureAdjust {
    
    long r2;
    
    public SphericTemperatureAdjust(int cx, int cy, int cz, int r, int value) {
        super(cx, cy, cz, r, value);
        r2 = r * r;
    }
    
    public SphericTemperatureAdjust(FriendlyByteBuf buffer) {
        super(buffer);
        r2 = r * r;
    }
    
    public SphericTemperatureAdjust(CompoundTag nc) {
        super(nc);
        r2 = r * r;
    }
    
    public SphericTemperatureAdjust(BlockPos heatPos, int range, int tempMod) {
        super(heatPos, range, tempMod);
        r2 = r * r;
    }
    
    @Override
    public boolean isEffective(int x, int y, int z) {
        long l = (long) Math.pow(x - cx, 2);
        l += (long) Math.pow(y - cy, 2);
        l += (long) Math.pow(z - cz, 2);
        return l <= r;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = serializeNBTData();
        nbt.putInt("type", 2);
        return nbt;
    }
    
    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(2);
        super.serializeData(buffer);
    }
    
}
