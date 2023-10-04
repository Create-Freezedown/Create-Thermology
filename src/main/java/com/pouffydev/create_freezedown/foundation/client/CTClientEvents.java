package com.pouffydev.create_freezedown.foundation.client;

//import generator.util.CTClientProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.pouffydev.create_freezedown.Thermology.ID;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CTClientEvents {
    
    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders ev)
    {
        //CTClientProperties.initModels();
    }
}
