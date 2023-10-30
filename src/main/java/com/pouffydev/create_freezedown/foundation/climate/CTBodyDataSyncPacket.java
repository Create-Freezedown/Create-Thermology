package com.pouffydev.create_freezedown.foundation.climate;

import com.pouffydev.create_freezedown.foundation.climate.temperature.TemperatureCore;
import com.pouffydev.create_freezedown.foundation.util.ClientUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CTBodyDataSyncPacket {
    private final CompoundTag data;
    
    public CTBodyDataSyncPacket(Player pe) {
        this.data = TemperatureCore.getFHData(pe);
    }
    
    public CTBodyDataSyncPacket(FriendlyByteBuf buffer) {
        data = buffer.readNbt();
    }
    
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(data);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Update client-side nbt
            Level world = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientUtils::getWorld);
            Player player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientUtils::getPlayer);
            if (world != null) {
                TemperatureCore.setFHData(player, data);
            }
        });
        context.get().setPacketHandled(true);
    }
}
