package com.pouffydev.create_freezedown;

import com.mojang.logging.LogUtils;
import com.pouffydev.create_freezedown.content.fluids.OpenEndedPipeEffects;
import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankHeaters;
import com.pouffydev.create_freezedown.foundation.FreezedownRegistrate;
import com.pouffydev.create_freezedown.foundation.client.CTPartialModels;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Thermology.ID)
public class Thermology
{
    public static final String ID = "create_freezedown";
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static final FreezedownRegistrate REGISTRATE = FreezedownRegistrate.create(ID);
    
    public Thermology()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        CTItems.register();
        CTBlocks.register();
        CTBlockEntityTypes.register();
        CTFluids.register();
        //CTMultiblocks.init();
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CTClient.onCtorClient(modEventBus, forgeEventBus));
    }
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OpenEndedPipeEffects.register();
            BronzeTankHeaters.registerDefaults();
        });
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }
    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
}
