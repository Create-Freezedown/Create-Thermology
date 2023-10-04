package com.pouffydev.create_freezedown.content.kinetics.freezable.mixer;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class FreezableMixerBlockEntity extends MechanicalMixerBlockEntity {
    public FreezableMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
            basinChecker.scheduleUpdate();
            return;
        }
        
        float speed = Math.abs(getSpeed());
        if (running && level != null) {
            if (level.isClientSide && runningTicks == 20)
                renderParticles();
            
            if ((!level.isClientSide || isVirtual()) && runningTicks == 20) {
                if (processingTicks < 0) {
                    float recipeSpeed = 1;
                    if (currentRecipe instanceof ProcessingRecipe) {
                        int t = ((ProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                        if (t != 0)
                            recipeSpeed = t / 100f;
                    }
                    
                    processingTicks = Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512);
                    
                    Optional<BasinBlockEntity> basin = getBasin();
                    if (basin.isPresent()) {
                        Couple<SmartFluidTankBehaviour> tanks = basin.get()
                                .getTanks();
                        if (!tanks.getFirst()
                                .isEmpty()
                                || !tanks.getSecond()
                                .isEmpty())
                            level.playSound(null, worldPosition, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                                    SoundSource.BLOCKS, .75f, speed < 65 ? .75f : 1.5f);
                    }
                    
                } else {
                    processingTicks--;
                    if (processingTicks == 0) {
                        runningTicks++;
                        processingTicks = -1;
                        applyBasinRecipe();
                        sendData();
                    }
                }
            }
            
            if (runningTicks != 20)
                runningTicks++;
        }
    }
    
}
