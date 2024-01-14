package com.pouffydev.create_freezedown.foundation.util;

import com.pouffydev.create_freezedown.Thermology;
import com.pouffydev.create_freezedown.foundation.client.CTLang;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiUtils {
    public static ResourceLocation makeTextureLocation(String name) {
        return Thermology.asResource("textures/gui/" + name + ".png");
    }
    
    public static Component str(String s) {
        return CTLang.translate(s).component();
    }
    
    public static Component translateGui(String name, Object... args) {
        return CTLang.translate("gui." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateTooltip(String name, Object... args) {
        return CTLang.translate("tooltip." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateMessage(String name, Object... args) {
        return CTLang.translate("message." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateJeiCategory(String name, Object... args) {
        return CTLang.translate("gui.jei.category." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateResearchLevel(String name, Object... args) {
        return CTLang.translate("research.level." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateResearchCategoryName(String name, Object... args) {
        return CTLang.translate("research.category." + Thermology.ID + "." + name, args).component();
    }
    
    public static Component translateResearchCategoryDesc(String name, Object... args) {
        return CTLang.translate("research.category.desc." + Thermology.ID + "." + name, args).component();
    }
    public static String toTemperatureIntString(float celsus) {
        celsus=Math.max(-273.15f, celsus);
        return ((int)(celsus*10))/10+" °C";
    }
    public static String toTemperatureIntStringFahrenheit(float fahrenheit) {
        fahrenheit=Math.max(-273.15f, fahrenheit);
        return ((int)((fahrenheit*9/5+32)*10))/10+" °F";
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
