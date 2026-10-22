package com.vcwdfca.randomutil.coremod.mixins;

import net.minecraftforge.fluids.FluidStack;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

import goodgenerator.blocks.tileEntity.MTELargeEssentiaSmeltery;

/**
 * TODO: Remove this
 * once
 * <a href="https://github.com/GTNewHorizons/GT5-Unofficial/pull/8519">GTNewHorizons/GT5-Unofficial#8519</a> has been
 * merged and shipped in an official release.
 */
@Deprecated
@SuppressWarnings("UnusedMixin")
@Mixin(value = MTELargeEssentiaSmeltery.class, remap = false)
public class MixinLES {

    @Redirect(
        method = "checkProcessing_EM",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraftforge/fluids/FluidStack;amount:I",
            opcode = Opcodes.PUTFIELD,
            ordinal = 2))
    private void fixLESWrongDeplete(FluidStack fluidStack, int value, @Local(name = "p") int p) {
        fluidStack.amount -= p * 1000;
    }
}
