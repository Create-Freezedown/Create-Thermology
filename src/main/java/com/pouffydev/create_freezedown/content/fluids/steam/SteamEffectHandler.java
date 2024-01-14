package com.pouffydev.create_freezedown.content.fluids.steam;

import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public class SteamEffectHandler implements OpenEndedPipe.IEffectHandler {
    @Override
    public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        return fluid.getFluid() instanceof SteamFluid;
    }
    @Override
    public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        if (!(pipe.getWorld() instanceof ServerLevel level))
            return;
        var pos = pipe.getOutputPos();
        var pipePos = pipe.getPos();
        spawnParticles(level, pipePos, pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnParticles(ServerLevel level, BlockPos sourcePos, BlockPos pipeOutputPos) {
        Direction facing = SteamEngineBlock.getFacing(level.getBlockState(sourcePos));
        
        Vec3 offset = VecHelper.rotate(new Vec3(0, 0, 1).add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                .multiply(1, 1, 0)
                .normalize()
                .scale(.5f)), AngleHelper.verticalAngle(facing), Direction.Axis.X);
        offset = VecHelper.rotate(offset, AngleHelper.horizontalAngle(facing), Direction.Axis.Y);
        Vec3 v = offset.scale(.5f)
                .add(Vec3.atCenterOf(pipeOutputPos));
        Vec3 m = offset.subtract(Vec3.atLowerCornerOf(facing.getNormal())
                .scale(.75f));
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }
}
