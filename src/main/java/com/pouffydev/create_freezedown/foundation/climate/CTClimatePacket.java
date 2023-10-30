package com.pouffydev.create_freezedown.foundation.climate;

import com.pouffydev.create_freezedown.foundation.climate.data.ClientForecastData;
import com.pouffydev.create_freezedown.foundation.climate.data.ClimateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CTClimatePacket {
    private final short[] data;
    private final long sec;
    public CTClimatePacket(ClimateData climateData) {
        data = climateData.getFrames();
        sec= climateData.getSec();
    }
    public CTClimatePacket() {
        data = new short[0];
        sec=0;
    }
    
    public CTClimatePacket(FriendlyByteBuf buffer) {
        data=SerializeUtil.readShortArray(buffer);
        sec=buffer.readVarLong();
    }
    
    public void encode(FriendlyByteBuf buffer) {
        SerializeUtil.writeShortArray(buffer, data);
        buffer.writeVarLong(sec);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Update client-side nbt
            if(data.length==0) {
                ClientForecastData.clear();
                return;
            }
            int max=Math.min(ClientForecastData.tfs.length, data.length);
            for(int i=0;i<max;i++) {
                ClientForecastData.tfs[i]= ClimateData.TemperatureFrame.unpack(data[i]);
            }
            ClientForecastData.secs=sec;
        });
        context.get().setPacketHandled(true);
    }
}
