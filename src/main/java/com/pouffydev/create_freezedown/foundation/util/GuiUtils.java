package com.pouffydev.create_freezedown.foundation.util;

import com.pouffydev.create_freezedown.Thermology;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiUtils {
    public static ResourceLocation makeTextureLocation(String name) {
        return Thermology.asResource("textures/gui/" + name + ".png");
    }
    
    public static Component str(String s) {
        return Lang.translate(s).component();
    }
    
    public static Component translateGui(String name, Object... args) {
        return Lang.translate("gui." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateTooltip(String name, Object... args) {
        return Lang.translate("tooltip." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateMessage(String name, Object... args) {
        return Lang.translate("message." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateJeiCategory(String name, Object... args) {
        return Lang.translate("gui.jei.category." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateResearchLevel(String name, Object... args) {
        return Lang.translate("research.level." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateResearchCategoryName(String name, Object... args) {
        return Lang.translate("research.category." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateResearchCategoryDesc(String name, Object... args) {
        return Lang.translate("research.category.desc." + Thermology.ID + "." + name, args).component();
    }
    public static String toTemperatureIntString(float celsus) {
        celsus=Math.max(-273.15f, celsus);
        return ((int)(celsus*10))/10+" °C";
    }
    public static String toTemperatureFloatString(float celsus) {
        celsus=Math.max(-273.15f, celsus);
        return celsus+" °C";
    }
    public static String toTemperatureDeltaIntString(float celsus) {
        celsus=Math.max(-273.15f, celsus);
        return ((int)(celsus*10))/10+" °C";
    }
    public static String toTemperatureDeltaFloatString(float celsus) {
        celsus=Math.max(-273.15f, celsus);
        return celsus+" °C";
    }
}
