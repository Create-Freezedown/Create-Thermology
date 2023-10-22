package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.foundation.client.CTPartialModels;
import com.pouffydev.create_freezedown.foundation.client.CTSpriteShifts;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CTClient {
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CTClient::clientInit);
    }
    public static void clientInit(final FMLClientSetupEvent event) {
        CTPartialModels.init();
        CTSpriteShifts.init();
    }
}
