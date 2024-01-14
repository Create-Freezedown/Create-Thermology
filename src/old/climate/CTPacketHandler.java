package com.pouffydev.create_freezedown.foundation.climate;

import com.pouffydev.create_freezedown.Thermology;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CTPacketHandler {
    
    private static SimpleChannel CHANNEL;
    
    public static void send(PacketDistributor.PacketTarget target, Object message) {
        CHANNEL.send(target, message);
    }
    
    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }
    
    public static SimpleChannel get() {
        return CHANNEL;
    }
    public static final int NETWORK_VERSION = 3;
    public static final ResourceLocation CHANNEL_NAME = Thermology.asResource("main");
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    public static SimpleChannel getChannel() {
        return CHANNEL;
    }
    public static void register() {
        System.out.println("[TWR Version Check] CT Network Version: " + NETWORK_VERSION);
        CHANNEL = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();
        int id = 0;
        
        // CHANNEL.registerMessage(id++, ChunkWatchPacket.class,
        // ChunkWatchPacket::encode, ChunkWatchPacket::new, ChunkWatchPacket::handle);
        // CHANNEL.registerMessage(id++, ChunkUnwatchPacket.class,
        // ChunkUnwatchPacket::encode, ChunkUnwatchPacket::new,
        // ChunkUnwatchPacket::handle);
        // CHANNEL.registerMessage(id++, TemperatureChangePacket.class,
        // TemperatureChangePacket::encode, TemperatureChangePacket::new,
        // TemperatureChangePacket::handle);
        CHANNEL.registerMessage(id++, CTBodyDataSyncPacket.class, CTBodyDataSyncPacket::encode, CTBodyDataSyncPacket::new,
                CTBodyDataSyncPacket::handle);
        CHANNEL.registerMessage(id++, CTDatapackSyncPacket.class, CTDatapackSyncPacket::encode,
                CTDatapackSyncPacket::new, CTDatapackSyncPacket::handle);
        //CHANNEL.registerMessage(id++, FHResearchRegistrtySyncPacket.class, FHResearchRegistrtySyncPacket::encode,
        //        FHResearchRegistrtySyncPacket::new, FHResearchRegistrtySyncPacket::handle);
        //CHANNEL.registerMessage(id++, FHResearchDataSyncPacket.class, FHResearchDataSyncPacket::encode,
        //        FHResearchDataSyncPacket::new, FHResearchDataSyncPacket::handle);
        //CHANNEL.registerMessage(id++, FHResearchDataUpdatePacket.class, FHResearchDataUpdatePacket::encode,
        //        FHResearchDataUpdatePacket::new, FHResearchDataUpdatePacket::handle);
        //CHANNEL.registerMessage(id++, FHClueProgressSyncPacket.class, FHClueProgressSyncPacket::encode,
        //        FHClueProgressSyncPacket::new, FHClueProgressSyncPacket::handle);
        CHANNEL.registerMessage(id++, CTClimatePacket.class, CTClimatePacket::encode, CTClimatePacket::new,
                CTClimatePacket::handle);
        //CHANNEL.registerMessage(id++, FHEffectTriggerPacket.class, FHEffectTriggerPacket::encode,
        //        FHEffectTriggerPacket::new, FHEffectTriggerPacket::handle);
        //CHANNEL.registerMessage(id++, FHResearchControlPacket.class, FHResearchControlPacket::encode,
        //        FHResearchControlPacket::new, FHResearchControlPacket::handle);
        //CHANNEL.registerMessage(id++, FHChangeActiveResearchPacket.class, FHChangeActiveResearchPacket::encode,
        //        FHChangeActiveResearchPacket::new, FHChangeActiveResearchPacket::handle);
        //CHANNEL.registerMessage(id++, FHDrawingDeskOperationPacket.class, FHDrawingDeskOperationPacket::encode,
        //        FHDrawingDeskOperationPacket::new, FHDrawingDeskOperationPacket::handle);
        //CHANNEL.registerMessage(id++, FHEffectProgressSyncPacket.class, FHEffectProgressSyncPacket::encode,
        //        FHEffectProgressSyncPacket::new, FHEffectProgressSyncPacket::handle);
        //CHANNEL.registerMessage(id++, FHEnergyDataSyncPacket.class, FHEnergyDataSyncPacket::encode,
        //        FHEnergyDataSyncPacket::new, FHEnergyDataSyncPacket::handle);
        CHANNEL.registerMessage(id++, CTTemperatureDisplayPacket.class,CTTemperatureDisplayPacket::encode,
                CTTemperatureDisplayPacket::new, CTTemperatureDisplayPacket::handle);
        
    }
}
