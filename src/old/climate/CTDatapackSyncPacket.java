package com.pouffydev.create_freezedown.foundation.climate;

import com.pouffydev.create_freezedown.foundation.climate.data.CTDataManager;
import com.pouffydev.create_freezedown.foundation.climate.data.DataEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CTDatapackSyncPacket {
    DataEntry[] entries;
    
    public CTDatapackSyncPacket() {
        entries = CTDataManager.save();
    }
    
    public CTDatapackSyncPacket(FriendlyByteBuf buffer) {
        decode(buffer);
    }
    
    public void decode(FriendlyByteBuf buffer) {
        entries = new DataEntry[buffer.readVarInt()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new DataEntry(buffer);
        }
    }
    
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entries.length);
        for (DataEntry de : entries)
            de.encode(buffer);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Update client-side nbt
            CTDataManager.load(entries);
        });
        context.get().setPacketHandled(true);
    }
}
