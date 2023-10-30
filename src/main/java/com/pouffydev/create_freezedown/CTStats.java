package com.pouffydev.create_freezedown;

import com.pouffydev.create_freezedown.content.fluids.steam.pipe.SteamPumpBlock;
import com.pouffydev.create_freezedown.content.kinetics.cog_crank.CogCrankBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.item.KineticStats;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class CTStats extends KineticStats {
    public CTStats(Block block) {
        super(block);
    }
    
    public static @Nullable KineticStats create(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof IRotate || block instanceof SteamPumpBlock || block instanceof CogCrankBlock) {
                return new KineticStats(block);
            }
        }
        
        return null;
    }
}
