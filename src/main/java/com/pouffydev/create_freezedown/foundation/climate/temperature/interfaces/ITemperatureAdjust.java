package com.pouffydev.create_freezedown.foundation.climate.temperature.interfaces;

import com.pouffydev.create_freezedown.foundation.climate.data.CubicTemperatureAdjust;
import com.pouffydev.create_freezedown.foundation.climate.data.SphericTemperatureAdjust;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITemperatureAdjust extends INBTSerializable<CompoundTag> {
    
    /**
     * Get temperature at location, would check if it is in range.
     *
     * @param x the locate x<br>
     * @param y the locate y<br>
     * @param z the locate z<br>
     * @return temperature value at location<br>
     */
    int getTemperatureAt(int x, int y, int z);
    
    /**
     * Get temperature at location, would check if it is in range.
     *
     * @param pos the location<br>
     * @return temperature value at location<br>
     */
    default int getTemperatureAt(BlockPos pos) {
        return getTemperatureAt(pos.getX(), pos.getY(), pos.getZ());
    }
    
    ;
    
    /**
     * Checks if location is in range(or, this adjust is effective for this location).<br>
     *
     * @param x the x<br>
     * @param y the y<br>
     * @param z the z<br>
     * @return if this adjust is effective for location, true.
     */
    boolean isEffective(int x, int y, int z);
    
    /**
     * Checks if location is in range(or, this adjust is effective for this location).<br>
     *
     * @param bp the location<br>
     * @return if this adjust is effective for location, true.
     */
    default boolean isEffective(BlockPos pos) {
        return isEffective(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Serialize.
     *
     * @param buffer the buffer<br>
     */
    void serialize(FriendlyByteBuf buffer);
    
    /**
     * Deserialize.
     *
     * @param buffer the buffer<br>
     */
    void deserialize(FriendlyByteBuf buffer);
    
    /**
     * Factory construct temperature adjust from packet buffer.<br>
     *
     * @param buffer the buffer<br>
     * @return returns adjust
     */
    public static ITemperatureAdjust valueOf(FriendlyByteBuf buffer) {
        int packetId = buffer.readVarInt();
        switch (packetId) {
            case 1:
                return new CubicTemperatureAdjust(buffer);
            case 2:
                return new SphericTemperatureAdjust(buffer);
            default:
                return new CubicTemperatureAdjust(buffer);
        }
    }
    
    /**
     * Factory construct temperature adjust from NBT<br>
     *
     * @param nc the nbt compound<br>
     * @return returns adjust
     */
    static ITemperatureAdjust valueOf(CompoundTag nc) {
        switch (nc.getInt("type")) {
            case 1:
                return new CubicTemperatureAdjust(nc);
            case 2:
                return new SphericTemperatureAdjust(nc);
            default:
                return new CubicTemperatureAdjust(nc);
        }
    }
    
    /**
     * Get center X.
     *
     * @return center X<br>
     */
    int getCenterX();
    
    /**
     * Get center Y.
     *
     * @return center Y<br>
     */
    int getCenterY();
    
    /**
     * Get center Z.
     *
     * @return center Z<br>
     */
    int getCenterZ();
    
    int getRadius();
    
    /**
     * Get value at location, wont do range check.
     *
     * @param pos the location<br>
     * @return value for that location<br>
     */
    float getValueAt(BlockPos pos);
    
    void setValue(int value);
}
