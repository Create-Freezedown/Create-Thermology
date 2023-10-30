package com.pouffydev.create_freezedown.foundation.climate.temperature;

import com.pouffydev.create_freezedown.foundation.climate.data.CTDataManager;
import com.pouffydev.create_freezedown.foundation.climate.temperature.data.BlockTempData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TemperatureSimulator {
    public LevelChunk[] sections = new LevelChunk[8];
    public LevelChunkSection[] levelSections = new LevelChunkSection[8];
    public static final int range = 8;
    BlockPos origin;
    ServerLevel world;
    Random rnd;
    private static final int n = 4168;
    private static final int rdiff = 10;
    private static final float v0 = 0.4F;
    private static final VoxelShape EMPTY = Shapes.empty();
    private static final VoxelShape FULL = Shapes.empty();
    private double[] qx = new double[4168];
    private double[] qy = new double[4168];
    private double[] qz = new double[4168];
    private static float[] vx = new float[4168];
    private static float[] vy = new float[4168];
    private static float[] vz = new float[4168];
    private int[] vid = new int[4168];
    private static final int num_rounds = 20;
    public Map<BlockState, CachedBlockInfo> info = new HashMap();
    public Map<BlockPos, CachedBlockInfo> posinfo = new HashMap();
    
    public static void init() {
    }
    
    public TemperatureSimulator(ServerPlayer player) {
        int sourceX = (int)player.getX();
        int sourceY = (int)player.getY();
        int sourceZ = (int)player.getZ();
        int offsetN = sourceZ - 8;
        int offsetW = sourceX - 8;
        int offsetD = sourceY - 8;
        int chunkOffsetW = offsetW >> 4;
        int chunkOffsetN = offsetN >> 4;
        int chunkOffsetD = offsetD >> 4;
        this.origin = new BlockPos(chunkOffsetW + 1 << 4, chunkOffsetD + 1 << 4, chunkOffsetN + 1 << 4);
        int i = 0;
        this.world = player.getLevel();
        
        for(int x = chunkOffsetW; x <= chunkOffsetW + 1; ++x) {
            for(int z = chunkOffsetN; z <= chunkOffsetN + 1; ++z) {
                LevelChunkSection[] css = this.world.getChunk(x, z).getSections();
                LevelChunkSection[] var15 = css;
                int var16 = css.length;
                
                for(int var17 = 0; var17 < var16; ++var17) {
                    LevelChunkSection cs = var15[var17];
                    if (cs != null) {
                        int ynum = cs.bottomBlockY() >> 4;
                        if (ynum == chunkOffsetD) {
                            this.levelSections[i] = cs;
                        }
                        
                        if (ynum == chunkOffsetD + 1) {
                            this.levelSections[i + 1] = cs;
                        }
                    }
                }
                
                i += 2;
            }
        }
        
        this.rnd = new Random(player.blockPosition().asLong());
    }
    
    public BlockState getBlock(int x, int y, int z) {
        int i = 0;
        if (x >= 0) {
            i += 4;
        } else {
            x += 16;
        }
        
        if (z >= 0) {
            i += 2;
        } else {
            z += 16;
        }
        
        if (y >= 0) {
            ++i;
        } else {
            y += 16;
        }
        
        if (x < 16 && y < 16 && z < 16 && x >= 0 && y >= 0 && z >= 0) {
            LevelChunk current = this.sections[i];
            if (current == null) {
                return Blocks.AIR.defaultBlockState();
            } else {
                try {
                    BlockPos pos = new BlockPos(x, y, z);
                    return current.getBlockState(pos);
                } catch (Exception var7) {
                    throw new RuntimeException("Failed to get block at" + x + "," + y + "," + z);
                }
            }
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }
    
    private CachedBlockInfo getInfoCached(BlockPos pos) {
        return (CachedBlockInfo)this.posinfo.computeIfAbsent(pos, (p) -> {
            return this.getInfo(p);
        });
    }
    
    private CachedBlockInfo getInfo(BlockPos pos) {
        BlockPos ofregion = pos.subtract(this.origin);
        BlockState bs = this.getBlock(ofregion.getX(), ofregion.getY(), ofregion.getZ());
        return this.info.computeIfAbsent(bs, (s) -> {
            return this.getInfo(pos, s);
        });
    }
    
    private CachedBlockInfo getInfo(BlockPos pos, BlockState bs) {
        BlockTempData b = CTDataManager.getBlockData(bs.getBlock());
        if (b == null) {
            return new CachedBlockInfo(bs.getBlockSupportShape(this.world, pos));
        } else {
            float cblocktemp = 0.0F;
            if (b.isLit()) {
                boolean litOrActive = false;
                if (bs.hasProperty(BlockStateProperties.LIT) && (Boolean)bs.getValue(BlockStateProperties.LIT)) {
                    litOrActive = true;
                }
                
                if (litOrActive) {
                    cblocktemp += b.getTemp();
                }
            } else {
                cblocktemp += b.getTemp();
            }
            
            if (b.isLevel()) {
                if (bs.hasProperty(BlockStateProperties.LEVEL)) {
                    cblocktemp *= (float)(((Integer)bs.getValue(BlockStateProperties.LEVEL) + 1) / 16);
                } else if (bs.hasProperty(BlockStateProperties.LEVEL_COMPOSTER)) {
                    cblocktemp *= (float)(((Integer)bs.getValue(BlockStateProperties.LEVEL_COMPOSTER) + 1) / 9);
                } else if (bs.hasProperty(BlockStateProperties.LEVEL_FLOWING)) {
                    cblocktemp *= (float)((Integer)bs.getValue(BlockStateProperties.LEVEL_FLOWING) / 8);
                } else if (bs.hasProperty(BlockStateProperties.LEVEL_CAULDRON)) {
                    cblocktemp *= (float)(((Integer)bs.getValue(BlockStateProperties.LEVEL_CAULDRON) + 1) / 4);
                }
            }
            
            return new CachedBlockInfo(bs.getBlockSupportShape(this.world, pos), cblocktemp);
        }
    }
    
    private boolean isBlockade(double x, double y, double z) {
        CachedBlockInfo info = getInfoCached(new BlockPos(x, y, z));
        if (info.shape == FULL)
            return true;
        if (info.shape == EMPTY)
            return false;
        return info.shape.bounds().contains(Mth.frac(x), Mth.frac(y), Mth.frac(z));
        
    }
    
    private float getHeat(double x, double y, double z) {
        return this.getInfoCached(new BlockPos(x, y, z)).temperature;
    }
    
    public float getBlockTemperature(double qx0, double qy0, double qz0) {
        for(int i = 0; i < 4168; this.vid[i] = i++) {
            this.qx[i] = qx0;
            this.qy[i] = qy0;
            this.qz[i] = qz0;
        }
        
        float heat = 0.0F;
        
        for(int round = 0; round < 20; ++round) {
            for(int i = 0; i < 4168; ++i) {
                if (this.isBlockade(this.qx[i] + (double)vx[this.vid[i]], this.qy[i] + (double)vy[this.vid[i]], this.qz[i] + (double)vz[this.vid[i]])) {
                    this.vid[i] = this.rnd.nextInt(4168);
                }
                
                this.qx[i] += (double)vx[this.vid[i]];
                this.qy[i] += (double)vy[this.vid[i]];
                this.qz[i] += (double)vz[this.vid[i]];
                heat = (float)((double)heat + (double)this.getHeat(this.qx[i], this.qy[i], this.qz[i]) * Mth.lerp(Mth.clamp((double)vy[this.vid[i]], 0.0, 0.4) * 2.5, 1.0, 0.5));
            }
        }
        
        return heat / 4168.0F;
    }
    
    static {
        int o = 0;
        
        for(int i = -10; i <= 10; ++i) {
            for(int j = -10; j <= 10; ++j) {
                for(int k = -10; k <= 10; ++k) {
                    if (i != 0 || j != 0 || k != 0) {
                        float x = (float)i * 1.0F / 10.0F;
                        float y = (float)j * 1.0F / 10.0F;
                        float z = (float)k * 1.0F / 10.0F;
                        float r = Mth.sqrt(x * x + y * y + z * z);
                        if (!(r > 1.0F)) {
                            vx[o] = x / r * 0.4F;
                            vy[o] = y / r * 0.4F;
                            vz[o] = z / r * 0.4F;
                            ++o;
                        }
                    }
                }
            }
        }
        
    }
    
    private static class CachedBlockInfo {
        VoxelShape shape;
        float temperature;
        
        public CachedBlockInfo(VoxelShape shape, float temperature) {
            this.shape = shape;
            this.temperature = temperature;
        }
        
        public CachedBlockInfo(VoxelShape shape) {
            this.shape = shape;
        }
    }
}
