package com.pouffydev.create_freezedown.content.kinetics.cog_crank;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlockEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;


public class CogCrankRenderer extends KineticBlockEntityRenderer<CogCrankBlockEntity> {
    public CogCrankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(CogCrankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        
        if (Backend.canUseInstancing(be.getLevel()))
            return;
        //Direction.Axis facing = be.getBlockState().getValue(AXIS);
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        BlockState blockState = be.getBlockState();
        //kineticRotationTransform(be.getRenderedHandle(), be, facing, be.getIndependentAngle(partialTicks), light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        renderHandle(be, ms, light, blockState,  partialTicks, vb);
    }
    private void renderHandle(CogCrankBlockEntity be, PoseStack ms, int light, BlockState blockState, float partialTicks, VertexConsumer vb) {
        SuperByteBuffer handle = CachedBufferer.block(blockState);
        kineticRotationTransform(be.getRenderedHandle(), be, getRotationAxisOf(be),  be.getIndependentAngle(partialTicks), light);
        handle.renderInto(ms, vb);
    }
}
