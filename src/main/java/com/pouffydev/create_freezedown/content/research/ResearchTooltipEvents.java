package com.pouffydev.create_freezedown.content.research;

import com.pouffydev.create_freezedown.Thermology;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Thermology.ID, value = Dist.CLIENT)
public class ResearchTooltipEvents {
    @SubscribeEvent
    public static void addTooltipToUnresearchedItems(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        
        boolean itemUse = ResearchUtils.canItemBeResearched(item, ResearchTypes.itemUse);
        boolean itemSwing = ResearchUtils.canItemBeResearched(item, ResearchTypes.itemSwing);
        boolean blockPlace = ResearchUtils.canItemBeResearched(item, ResearchTypes.blockPlace);
        if (event.getEntity() == null) {
            return;
        }
        boolean isResearched = ResearchUtils.isItemResearched(item, event.getEntity());
        if (itemUse || itemSwing || blockPlace) {
            List<Component> tooltip = event.getToolTip();
            if (!isResearched) {
                tooltip.add(Component.nullToEmpty("§c§oThis item is not researched."));
            }
        }
    }
}
