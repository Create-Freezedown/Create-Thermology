package com.pouffydev.create_freezedown.foundation.mixin.research;

import com.pouffydev.create_freezedown.content.research.ResearchTypes;
import com.pouffydev.create_freezedown.content.research.ResearchUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    //Add a check in itemInteract to see if the item is in the research list and the research is unlocked in player data.
    
    //@Inject(method = "use", at = @At("TAIL"), cancellable = true)
    //public void itemUse(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    //    InteractionResult success = InteractionResult.SUCCESS;
    //    InteractionResult failure = InteractionResult.FAIL;
    //    if (!(ResearchUtils.isItemResearched((Item) (Object) this, player, ResearchTypes.itemUse))) {
    //        player.displayClientMessage(Component.nullToEmpty("You do not know how to use this item yet."), true);
    //        cir.setReturnValue(failure);
    //    } else if(!(ResearchUtils.isItemResearched((Item) (Object) this, player, ResearchTypes.blockPlace))) {
    //        player.displayClientMessage(Component.literal("You do not know how to place this block yet."), true);
    //        cir.setReturnValue(success);
    //    }else {
    //        cir.setReturnValue(success);
    //    }
    //}
    
    //@Inject(method = "interactLivingEntity", at = @At("TAIL"), cancellable = true)
    //public void entityInteract(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand, CallbackInfoReturnable<InteractionResult> cir) {
    //    InteractionResult success = InteractionResult.SUCCESS;
    //    InteractionResult failure = InteractionResult.FAIL;
    //    if (ResearchUtils.isEntityResearched(pInteractionTarget, pPlayer, ResearchTypes.entityInteract)) {
    //        cir.setReturnValue(success);
    //    } else {
    //        pPlayer.displayClientMessage(Component.literal("You do not know how to interact with this entity yet."), true);
    //        cir.setReturnValue(failure);
    //    }
    //}
}
