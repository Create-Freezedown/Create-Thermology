package com.pouffydev.create_freezedown.content.fluids;

import com.pouffydev.create_freezedown.CTFluids;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public class OpenEndedPipeEffects {
    public static void register() {
        OpenEndedPipe.registerEffectHandler(new OpenEndedPipe.IEffectHandler() {
            @Override
            public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
                return fluid.getFluid().isSame(CTFluids.steam.get());
            }
            @Override
            public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
                var pos = pipe.getOutputPos();
                var pipePos = pipe.getPos();
                ServerLevel level = (ServerLevel) pipe.getWorld();
                spawnParticles(level, pipePos, pos);
            }
            
            @OnlyIn(Dist.CLIENT)
            private void spawnParticles(ServerLevel level, BlockPos sourcePos, BlockPos pipeOutputPos) {
                Vec3 offset = VecHelper.rotate(new Vec3(0, 0, 1).add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                        .multiply(1, 1, 0)
                        .normalize()
                        .scale(.5f)), 1, Direction.Axis.X);
                offset = VecHelper.rotate(offset, 1, Direction.Axis.Y);
                Vec3 v = offset.scale(.5f)
                        .add(Vec3.atCenterOf(pipeOutputPos));
                Vec3 m = offset.subtract(Vec3.atLowerCornerOf(pipeOutputPos)
                        .scale(.75f));
                level.addParticle(ParticleTypes.CLOUD, v.x, v.y, v.z, m.x, m.y, m.z);
            }
        });
    }
}
