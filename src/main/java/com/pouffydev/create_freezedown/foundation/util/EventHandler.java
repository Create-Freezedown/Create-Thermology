package com.pouffydev.create_freezedown.foundation.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

public class EventHandler {
    /**
     * During {@link ServerLevel#tickChunk(LevelChunk, int)}, places additional snow layers
     */
    public static void placeExtraSnow(ServerLevel level, ChunkAccess chunk)
    {
        if (level.random.nextInt(4) == 0)
        {
            final int blockX = chunk.getPos().getMinBlockX();
            final int blockZ = chunk.getPos().getMinBlockZ();
            final BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(blockX, 0, blockZ, 15));
            final BlockState state = level.getBlockState(pos);
            final Biome biome = level.getBiome(pos).value();
            if (level.isRaining() && biome.coldEnoughToSnow(pos) && level.getBrightness(LightLayer.BLOCK, pos) < 10)
            {
                if (state.getBlock() == Blocks.SNOW)
                {
                    // Stack snow layers
                    final int layers = state.getValue(BlockStateProperties.LAYERS);
                    if (layers < 5)
                    {
                        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LAYERS, 1 + layers));
                    }
                    
                    //final BlockPos belowPos = pos.below();
                    //final BlockState belowState = level.getBlockState(belowPos);
                    //final Block replacementBlock = PrimalWinterBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(belowState.getBlock(), () -> null).get();
                    //if (replacementBlock != null)
                    //{
                    //    level.setBlockAndUpdate(belowPos, replacementBlock.defaultBlockState());
                    //}
                }
            }
        }
    }
}
