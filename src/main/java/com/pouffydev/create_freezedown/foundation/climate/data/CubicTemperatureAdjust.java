package com.pouffydev.create_freezedown.foundation.climate.data;

import com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces.ITemperatureAdjust;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class CubicTemperatureAdjust implements ITemperatureAdjust {
    int cx;
    int cy;
    int cz;
    int r;
    int value;
    
    public CubicTemperatureAdjust(int cx, int cy, int cz, int r, int value) {
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.r = r;
        this.value = value;
    }
    
    public CubicTemperatureAdjust(FriendlyByteBuf buffer) {
        deserialize(buffer);
    }
    
    public CubicTemperatureAdjust(CompoundTag nc) {
        deserializeNBT(nc);
    }
    
    public CubicTemperatureAdjust(BlockPos heatPos, int range, int tempMod) {
        this(heatPos.getX(), heatPos.getY(), heatPos.getZ(), range, tempMod);
    }
    
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = serializeNBTData();
        nbt.putInt("type", 1);
        return nbt;
    }
    
    protected CompoundTag serializeNBTData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putIntArray("location", new int[]{cx, cy, cz});
        nbt.putInt("range", r);
        nbt.putInt("value", value);
        return nbt;
    }
    
    public int getCenterX() {
        return cx;
    }
    
    public int getCenterY() {
        return cy;
    }
    
    public int getCenterZ() {
        return cz;
    }
    
    public int getRadius() {
        return r;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int[] loc = nbt.getIntArray("location");
        cx = loc[0];
        cy = loc[1];
        cz = loc[2];
        r = nbt.getInt("range");
        value = nbt.getInt("value");
    }
    
    @Override
    public int getTemperatureAt(int x, int y, int z) {
        if (isEffective(x, y, z))
            return value;
        return 0;
    }
    
    @Override
    public boolean isEffective(int x, int y, int z) {
        if (Math.abs(x - cx) <= r && Math.abs(y - cy) <= r && Math.abs(z - cz) <= r)
            return true;
        return false;
    }
    
    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeVarInt(1);//packet id
        serializeData(buffer);
    }
    
    protected void serializeData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(cx);
        buffer.writeVarInt(cy);
        buffer.writeVarInt(cz);
        buffer.writeVarInt(r);
        buffer.writeByte(value);
    }
    
    @Override
    public void deserialize(FriendlyByteBuf buffer) {
        cx = buffer.readVarInt();
        cy = buffer.readVarInt();
        cz = buffer.readVarInt();
        r = buffer.readVarInt();
        value = buffer.readByte();
    }
    
    @Override
    public float getValueAt(BlockPos pos) {
        return value;
    }
    
    @Override
    public void setValue(int value) {
        this.value = value;
    }
    
}
