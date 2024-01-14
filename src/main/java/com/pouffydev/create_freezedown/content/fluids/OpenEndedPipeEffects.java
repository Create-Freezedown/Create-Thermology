package com.pouffydev.create_freezedown.content.fluids;

import com.pouffydev.create_freezedown.CTFluids;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import net.minecraft.server.level.ServerLevel;
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
                var pipePos = pipe.getPos();
                ServerLevel level = (ServerLevel) pipe.getWorld();
                //changeTemp(level, pipePos);
            }
            
            //private void changeTemp(Level level, BlockPos pos) {
            //    ChunkData.addSphericTempAdjust(level,
            //            pos,
            //            5,
            //            25);
            //}
        });
    }
}
