package com.pouffydev.create_freezedown.content.kinetics.cog_crank;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.pouffydev.create_freezedown.CTBlocks;
import com.pouffydev.create_freezedown.foundation.client.CTPartialModels;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CogCrankBlockEntity extends GeneratingKineticBlockEntity {
    
    public int inUse;
    public boolean backwards;
    public float independentAngle;
    public float chasingVelocity;
    
    public CogCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public void turn(boolean back) {
        boolean update = false;
        
        if (getGeneratedSpeed() == 0 || back != backwards)
            update = true;
        
        inUse = 10;
        this.backwards = back;
        if (update && !level.isClientSide)
            updateGeneratedRotation();
    }
    
    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }
    
    @Override
    public float getGeneratedSpeed() {
        Block block = getBlockState().getBlock();
        if (!(block instanceof CogCrankBlock))
            return 0;
        CogCrankBlock crank = (CogCrankBlock) block;
        int speed = (inUse == 0 ? 0 : clockwise() ? -1 : 1) * crank.getRotationSpeed();
        return speed;
    }
    
    protected boolean clockwise() {
        return backwards;
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("InUse", inUse);
        compound.putBoolean("Backwards", backwards);
        super.write(compound, clientPacket);
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        inUse = compound.getInt("InUse");
        backwards = compound.getBoolean("Backwards");
        super.read(compound, clientPacket);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;
        
        if (inUse > 0) {
            inUse--;
            
            if (inUse == 0 && !level.isClientSide) {
                sequenceContext = null;
                updateGeneratedRotation();
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    public SuperByteBuffer getRenderedHandle() {
        BlockState blockState = getBlockState();
        Direction axis = blockState.getOptionalValue(MechanicalCrafterBlock.HORIZONTAL_FACING).orElse(Direction.UP);
        return CachedBufferer.partialFacing(CTPartialModels.HAND_CRANK_HANDLE, blockState, axis.getOpposite());
    }
    
    @OnlyIn(Dist.CLIENT)
    public Instancer<ModelData> getRenderedHandleInstance(Material<ModelData> material) {
        BlockState blockState = getBlockState();
        Direction axis = blockState.getOptionalValue(MechanicalCrafterBlock.HORIZONTAL_FACING).orElse(Direction.UP);
        return material.getModel(CTPartialModels.HAND_CRANK_HANDLE, blockState, axis.getOpposite());
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderShaft() {
        return false;
    }
    
    @Override
    protected Block getStressConfigKey() {
        return CTBlocks.cogCrank.has(getBlockState()) ? CTBlocks.cogCrank.get()
                : AllBlocks.COPPER_VALVE_HANDLE.get();
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        if (inUse > 0 && AnimationTickHolder.getTicks() % 10 == 0) {
            if (!CTBlocks.cogCrank.has(getBlockState()))
                return;
            AllSoundEvents.CRANKING.playAt(level, worldPosition, (inUse) / 2.5f, .65f + (10 - inUse) / 10f, true);
        }
    }
}
