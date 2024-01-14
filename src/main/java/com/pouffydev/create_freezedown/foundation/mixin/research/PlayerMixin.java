package com.pouffydev.create_freezedown.foundation.mixin.research;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    public PlayerMixin() {
    }
    @Unique
    private static final String TAG_MONEY = "Money";
    
    @Inject(at = @At("TAIL"), method = "tick")
    private void tickDataCheck(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        CompoundTag compoundTag = player.getPersistentData();
        CompoundTag researchTag = compoundTag.getCompound("unlocked_research");
        CompoundTag itemUseTag = researchTag.getCompound("item_use");
        CompoundTag itemSwingTag = researchTag.getCompound("item_swing");
        CompoundTag blockPlaceTag = researchTag.getCompound("block_place");
        
        if (!compoundTag.contains("unlocked_research")) {
            compoundTag.put("unlocked_research", new CompoundTag());
        }
        if (!researchTag.contains("item_use")) {
            researchTag.put("item_use", new CompoundTag());
        }
        if (!researchTag.contains("item_swing")) {
            researchTag.put("item_swing", new CompoundTag());
        }
        if (!researchTag.contains("block_place")) {
            researchTag.put("block_place", new CompoundTag());
        }
    }
    
}
