package com.pouffydev.create_freezedown.foundation.client;

import com.jozufozu.flywheel.core.PartialModel;
import com.pouffydev.create_freezedown.Thermology;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class CTPartialModels {
    public static final PartialModel
            HAND_CRANK_HANDLE = block("cog_crank/handle"),
            steamPipeCasing = block("steam_pipe/casing"),
            powerSuitHelmet = entity("powersuit_helmet"),
            powerSuitChestplate = entity("powersuit_chestplate"),
            powerSuitLeftArm = entity("powersuit_left_arm"),
            powerSuitRightArm = entity("powersuit_right_arm"),
            powerSuitLeftLeg = entity("powersuit_left_leg"),
            powerSuitRightLeg = entity("powersuit_right_leg")
            ;
    private static PartialModel block(String path) {
        return new PartialModel(Thermology.asResource("block/" + path));
    }
    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> steamPipeAttachments =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);
    
    static {
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("steam_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            steamPipeAttachments.put(type, map);
        }
    }
    private static PartialModel entity(String path) {
        return new PartialModel(Thermology.asResource("entity/" + path));
    }
    
    public static void init() {
        // init static fields
    }
}
