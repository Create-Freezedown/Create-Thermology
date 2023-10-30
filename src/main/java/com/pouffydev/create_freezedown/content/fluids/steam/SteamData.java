package com.pouffydev.create_freezedown.content.fluids.steam;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.UnaryOperator;

public class SteamData {
    private int minValue = 0;
    private int maxValue = 0;
    private int steamTier = 0;
    private int outputTemperature = 0;
    private MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars,
                                             ChatFormatting... styles) {
        MutableComponent base = useBlocksAsBars ? blockComponent(level) : barComponent(level);
        
        if (!forGoggles)
            return base;
        
        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;
        
        return Lang.translateDirect("steam." + label)
                .withStyle(style1)
                .append(Lang.translateDirect("steam." + label + "_dots")
                        .withStyle(style2))
                .append(base);
    }
    private MutableComponent blockComponent(int level) {
        return Components.literal(
                "" + "\u2588".repeat(minValue) + "\u2592".repeat(level - minValue) + "\u2591".repeat(maxValue - level));
    }
    private MutableComponent barComponent(int level) {
        return Components.empty()
                .append(bars(Math.max(0, minValue - 1), ChatFormatting.DARK_GREEN))
                .append(bars(minValue > 0 ? 1 : 0, ChatFormatting.GREEN))
                .append(bars(Math.max(0, level - minValue), ChatFormatting.DARK_GREEN))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.DARK_RED))
                .append(bars(Math.max(0, Math.min(18 - maxValue, ((maxValue / 5 + 1) * 5) - maxValue)), ChatFormatting.DARK_GRAY));
        
    }
    private MutableComponent bars(int level, ChatFormatting format) {
        return Components.literal(Strings.repeat('|', level))
                .withStyle(format);
    }
    @NotNull
    public MutableComponent getSteamLevelTextComponent() {
        int steamLevel = steamTier;
        
        return steamLevel == 0 ? Lang.translateDirect("steam.none").withStyle(ChatFormatting.GRAY)
                : (steamLevel == 1 ? Lang.translateDirect("steam.basic").withStyle(Style.EMPTY.withColor(0xea950a))
                : steamLevel == 2 ? Lang.translateDirect("steam.superheated").withStyle(Style.EMPTY.withColor(0x565dbd))
                : steamLevel == 3 ? Lang.translateDirect("steam.supercritical")
                : Lang.translateDirect("steam.lvl", String.valueOf(steamLevel)));
    }
    public MutableComponent getSteamComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("steam", steamTier, forGoggles, useBlocksAsBars, styles);
    }
    public MutableComponent getTemperatureComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("temperature", outputTemperature, forGoggles, useBlocksAsBars, styles);
    }
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize) {
        Component indent = Components.literal(IHaveGoggleInformation.spacing);
        Component indent2 = Components.literal(IHaveGoggleInformation.spacing + " ");
        
        tooltip.add(indent.plainCopy().append(Lang.translateDirect("steam.status", getSteamLevelTextComponent())));
        tooltip.add(indent2.plainCopy()
                .append(getTemperatureComponent(true, false)));
        tooltip.add(indent2.plainCopy()
                .append(getSteamComponent(true, false)));
        
        tooltip.add(Components.immutableEmpty());
        
        Lang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        
        return true;
    }
}
