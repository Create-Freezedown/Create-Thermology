package com.pouffydev.create_freezedown.content.kinetics.freezable.press;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import org.joml.Quaternionf;

public class FreezablePressInstance extends ShaftInstance<FreezablePressBlockEntity> implements DynamicInstance {
    
    private final OrientedData pressHead;
    
    public FreezablePressInstance(MaterialManager materialManager, FreezablePressBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        
        pressHead = materialManager.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(AllPartialModels.MECHANICAL_PRESS_HEAD, blockState)
                .createInstance();
        
        Quaternionf q = Axis.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(FreezablePressBlock.HORIZONTAL_FACING)));
        
        pressHead.setRotation(q);
        
        transformModels();
    }
    
    @Override
    public void beginFrame() {
        transformModels();
    }
    
    private void transformModels() {
        float renderedHeadOffset = getRenderedHeadOffset(blockEntity);
        
        pressHead.setPosition(getInstancePosition())
                .nudge(0, -renderedHeadOffset, 0);
    }
    
    private float getRenderedHeadOffset(FreezablePressBlockEntity press) {
        PressingBehaviour pressingBehaviour = press.getPressingBehaviour();
        return pressingBehaviour.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks())
                * pressingBehaviour.mode.headOffset;
    }
    
    @Override
    public void updateLight() {
        super.updateLight();
        
        relight(pos, pressHead);
    }
    
    @Override
    public void remove() {
        super.remove();
        pressHead.delete();
    }
}
