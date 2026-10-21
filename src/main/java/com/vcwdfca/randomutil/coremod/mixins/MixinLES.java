package com.vcwdfca.randomutil.coremod.mixins;

import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import goodgenerator.blocks.tileEntity.MTELargeEssentiaSmeltery;
import gregtech.api.recipe.check.CheckRecipeResult;

/**
 * TODO: Remove this once
 * <a href="https://github.com/GTNewHorizons/GT5-Unofficial/pull/8519">GTNewHorizons/GT5-Unofficial#8519</a> has been
 * merged and shipped in an official release.
 */
@Deprecated
@SuppressWarnings("UnusedMixin")
@Mixin(value = MTELargeEssentiaSmeltery.class, remap = false)
public class MixinLES {

    @Inject(
        method = "checkProcessing_EM",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/aspects/AspectList;add(Lthaumcraft/api/aspects/AspectList;)Lthaumcraft/api/aspects/AspectList;",
            ordinal = 5,
            shift = At.Shift.AFTER))
    private void fixLESWrongDeplete(CallbackInfoReturnable<CheckRecipeResult> cir,
        @Local(name = "fluidStack") FluidStack fluidStack, @Local(name = "p") int p) {
        fluidStack.amount -= p * 999;
    }
}
