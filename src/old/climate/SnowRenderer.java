package com.pouffydev.create_freezedown.foundation.climate;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

public class SnowRenderer {
    private final static float[] rainSizeXMemento = new float[1024];
    private final static float[] rainSizeZMemento = new float[1024];
    
    public SnowRenderer() {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float f1 = i - 16;
                float f2 = Mth.sqrt(f * f + f1 * f1);
                rainSizeXMemento[i << 5 | j] = -f1 / f2;
                rainSizeZMemento[i << 5 | j] = f / f2;
            }
        }
    }
    
    public static void render(Minecraft mc,
                              ClientLevel world,
                              LightTexture lightTexture,
                              int ticks,
                              float partialTicks,
                              double cameraX,
                              double cameraY,
                              double cameraZ) {
        float rainStrength = world.getRainLevel(partialTicks);
        lightTexture.turnOnLightLayer();
        
        int cameraBlockPosX = Mth.floor(cameraX);
        int cameraBlockPosY = Mth.floor(cameraY);
        int cameraBlockPosZ = Mth.floor(cameraZ);
        
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShaderFogColor(0.0F, 1.0F, 0.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        
        // The blizzard will be rendered in the block range
        // cameraX - renderRadius <= x <= cameraX + renderRadius
        // cameraZ - renderRadius <= z <= cameraZ + renderRadius
        // For y, it is the same rule, while in addition y should > *altitude of first solid block*
        int renderRadius = Minecraft.useFancyGraphics() ? 5 : 8;
        RenderSystem.depthMask(Minecraft.useFancyGraphics());
        
        int i1 = -1;
        float ticksAndPartialTicks = ticks + partialTicks;
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        Random random = new Random(
                (long) cameraBlockPosX * cameraBlockPosX * 3121
                        + cameraBlockPosZ * 45238971L ^ (long) cameraBlockPosZ * cameraBlockPosZ * 418711
                        + (int)(ticksAndPartialTicks * 13761)
        );
        for (int currentlyRenderingZ = cameraBlockPosZ - renderRadius;
             currentlyRenderingZ <= cameraBlockPosZ + renderRadius;
             ++currentlyRenderingZ) {
            for (int currentlyRenderingX = cameraBlockPosX - renderRadius;
                 currentlyRenderingX <= cameraBlockPosX + renderRadius;
                 ++currentlyRenderingX) {
                int rainSizeIdx = (currentlyRenderingZ - cameraBlockPosZ + 16) * 32 + currentlyRenderingX - cameraBlockPosX + 16;
                
                // Size of snowflake
                double rainSizeX = rainSizeXMemento[rainSizeIdx] * 0.5D;
                double rainSizeZ = rainSizeZMemento[rainSizeIdx] * 0.5D;
                blockPos.set(currentlyRenderingX, 0, currentlyRenderingZ);
                
                assert mc.level != null;
                int altitudeOfHighestSolidBlock = mc.level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ());
                int renderingYLowerBound = Math.max(cameraBlockPosY - renderRadius, altitudeOfHighestSolidBlock);
                int renderingYUpperBound = Math.max(cameraBlockPosY + renderRadius, altitudeOfHighestSolidBlock);
                
                int posY2 = Math.max(altitudeOfHighestSolidBlock, cameraBlockPosY);
                
                // If the ``non-blocked'' block is out of render radius,
                // nothing will be rendered.
                if (renderingYLowerBound != renderingYUpperBound) {
                    
                    blockPos.set(currentlyRenderingX, renderingYLowerBound, currentlyRenderingZ);
                    
                    if (i1 != 1) {
                        if (i1 >= 0) {
                            tesselator.end();
                        }
                        
                        i1 = 1;
                        mc.getTextureManager().bindForSetup(new ResourceLocation("minecraft:textures/environment/snow.png"));
                        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }
                    
                    float fallSpeed = (float) (random.nextDouble()
                            + ticksAndPartialTicks * (float) random.nextGaussian() * 0.3D);
                    blockPos.set(currentlyRenderingX, posY2, currentlyRenderingZ);
                    int j3 = LevelRenderer.getLightColor(world, blockPos);
                    float f5 = -((float)(ticks & 511) + ticksAndPartialTicks) / 512.0F;
                    float f6 = (float)(random.nextDouble() + (double)ticksAndPartialTicks * 0.01D * (double)((float)random.nextGaussian()));
                    float f7 = (float)(random.nextDouble() + (double)(ticksAndPartialTicks * (float)random.nextGaussian()) * 0.001D);
                    double d3 = (double)currentlyRenderingX + 0.5D - cameraX;
                    double d5 = (double)currentlyRenderingZ + 0.5D - cameraZ;
                    float f8 = (float)Math.sqrt(d3 * d3 + d5 * d5) / (float)renderRadius;
                    float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F) * rainStrength;
                    blockPos.set(currentlyRenderingX, i1, currentlyRenderingZ);
                    int k3 = LevelRenderer.getLightColor(world, blockPos);
                    int l3 = k3 >> 16 & '\uffff';
                    int i4 = k3 & '\uffff';
                    int j4 = (l3 * 3 + 240) / 4;
                    int k4 = (i4 * 3 + 240) / 4;
                    bufferBuilder.vertex(
                            currentlyRenderingX - cameraX - rainSizeX + 0.5D,
                            renderingYUpperBound - cameraY,
                            currentlyRenderingZ - cameraZ - rainSizeZ + 0.5D)
                            .uv(0.0F + f7, renderingYLowerBound * 0.25F - Math.abs(fallSpeed))
                            .color(1.0F, 1.0F, 1.0F, f9)
                            .uv2(j3)
                            .endVertex();
                    bufferBuilder.vertex(
                                    currentlyRenderingX - cameraX - rainSizeX + 0.5D,
                                    renderingYUpperBound - cameraY,
                                    currentlyRenderingZ - cameraZ - rainSizeZ + 0.5D)
                            .uv(0.0F + f7, renderingYLowerBound * 0.25F - Math.abs(fallSpeed))
                            .color(1.0F, 1.0F, 1.0F, f9)
                            .uv2(j3)
                            .endVertex();
                    bufferBuilder.vertex(
                                    currentlyRenderingX - cameraX - rainSizeX + 0.5D,
                                    renderingYUpperBound - cameraY,
                                    currentlyRenderingZ - cameraZ - rainSizeZ + 0.5D)
                            .uv(0.0F + f7, renderingYLowerBound * 0.25F - Math.abs(fallSpeed))
                            .color(1.0F, 1.0F, 1.0F, f9)
                            .uv2(j3)
                            .endVertex();
                    bufferBuilder.vertex(
                                    currentlyRenderingX - cameraX - rainSizeX + 0.5D,
                                    renderingYUpperBound - cameraY,
                                    currentlyRenderingZ - cameraZ - rainSizeZ + 0.5D)
                            .uv(0.0F + f7, renderingYLowerBound * 0.25F - Math.abs(fallSpeed))
                            .color(1.0F, 1.0F, 1.0F, f9)
                            .uv2(j3)
                            .endVertex();
                    //bufferBuilder.vertex(
                    //                currentlyRenderingX - cameraX - rainSizeX + 0.5D + random.nextGaussian() * 2,
                    //                renderingYUpperBound - cameraY,
                    //                currentlyRenderingZ - cameraZ - rainSizeZ + 0.5D + random.nextGaussian())
                    //        .uv(0.0F + f7, renderingYLowerBound * 0.25F - Math.abs(fallSpeed))
                    //        .color(1.0F, 1.0F, 1.0F, ticksAndPartialTicks0)
                    //        .uv2(j3)
                    //        .endVertex();
                    //bufferBuilder.vertex(
                    //                currentlyRenderingX - cameraX + rainSizeX + 0.5D + random.nextGaussian() * 2,
                    //                renderingYUpperBound - cameraY,
                    //                currentlyRenderingZ - cameraZ + rainSizeZ + 0.5D + random.nextGaussian())
                    //        .uv(1.0F + f7, renderingYLowerBound * 0.25F - Math.abs(fallSpeed))
                    //        .color(1.0F, 1.0F, 1.0F, ticksAndPartialTicks0)
                    //        .uv2(j3)
                    //        .endVertex();
                    //bufferBuilder.vertex(
                    //                currentlyRenderingX - cameraX + rainSizeX + 0.5D + random.nextGaussian() * 2,
                    //                renderingYLowerBound - cameraY,
                    //                currentlyRenderingZ - cameraZ + rainSizeZ + 0.5D + random.nextGaussian())
                    //        .uv(1.0F + f7, renderingYUpperBound * 0.25F - Math.abs(fallSpeed))
                    //        .color(1.0F, 1.0F, 1.0F, ticksAndPartialTicks0)
                    //        .uv2(j3)
                    //        .endVertex();
                    //bufferBuilder.vertex(
                    //                currentlyRenderingX - cameraX - rainSizeX + 0.5D + random.nextGaussian() * 2,
                    //                renderingYLowerBound - cameraY,
                    //                currentlyRenderingZ - cameraZ - rainSizeZ + 0.5D + random.nextGaussian())
                    //        .uv(0.0F + f7, renderingYUpperBound * 0.25F - Math.abs(fallSpeed))
                    //        .color(1.0F, 1.0F, 1.0F, ticksAndPartialTicks0)
                    //        .uv2(j3)
                    //        .endVertex();
                }
            }
        }
        
        if (i1 >= 0) {
            tesselator.end();
        }
        
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        lightTexture.turnOffLightLayer();
        RenderSystem.setShaderFogColor(0.0F, 0.0F, 0.0F);
    }
}
