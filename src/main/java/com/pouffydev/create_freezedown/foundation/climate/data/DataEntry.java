package com.pouffydev.create_freezedown.foundation.climate.data;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
public class DataEntry {
    CTDataManager.CTDataType type;
    String data;
    
    public DataEntry(CTDataManager.CTDataType type, JsonObject data) {
        this.type = type;
        this.data = data.toString();
    }
    
    public DataEntry(FriendlyByteBuf buffer) {
        this.type = CTDataManager.CTDataType.values()[buffer.readVarInt()];
        this.data = buffer.readUtf();
    }
    
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(type.ordinal());
        buffer.writeUtf(data);
    }
}
