package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.foundation.creative.CTItemTab;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.UnaryOperator;

import static com.pouffydev.create_freezedown.Thermology.REGISTRATE;


@SuppressWarnings({"unused", "inline", "SameParameterValue"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CTItems {
    static TagKey<Item> gems(String material) {
        return AllTags.forgeItemTag("gems/" + material);
    }
    static TagKey<Item> ingots(String material) {return AllTags.forgeItemTag("ingots/" + material.replace("_ingot", ""));}
    static TagKey<Item> nuggets(String material) {
        return AllTags.forgeItemTag("nuggets/" + material);
    }
    static TagKey<Item> plates(String material) {
        return AllTags.forgeItemTag("plates/" + material);
    }
    static TagKey<Item> plates() {
        return AllTags.forgeItemTag("plates");
    }
    static TagKey<Item> ingots() {
        return AllTags.forgeItemTag("ingots");
    }
    static TagKey<Item> nuggets() {return AllTags.forgeItemTag("nuggets");}
    static TagKey<Item> gems() {
        return AllTags.forgeItemTag("gems");
    }
    static {
        REGISTRATE.setCreativeTab(CTItemTab.BASE_CREATIVE_TAB);
    }
    private static ItemEntry<Item> sheet(String material) {return REGISTRATE.item(material + "_sheet", Item::new).properties(p->p).tag(plates(material)).tag(plates()).register();}
    private static ItemEntry<Item> ingot(String material) {return REGISTRATE.item(material + "_ingot", Item::new).properties(p->p).tag(ingots(material)).tag(ingots()).register();}
    private static ItemEntry<Item> nugget(String material) {return REGISTRATE.item(material + "_nugget", Item::new).properties(p->p).tag(nuggets(material)).tag(nuggets()).register();}
    
    public static final ItemEntry<Item>
    
            thermalloy_ingot = ingot("thermalloy"),
            thermalloy_nugget = nugget("thermalloy"),
            thermalloy_sheet = sheet("thermalloy")
                    
                    ;
    
    public static void register() {}
}
