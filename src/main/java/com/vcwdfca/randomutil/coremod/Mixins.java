package com.vcwdfca.randomutil.coremod;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

@SuppressWarnings("unused")
public enum Mixins implements IMixins {

    GT_LES(new MixinBuilder().setPhase(Phase.LATE)
        .addCommonMixins("MixinLES"));

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @NotNull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
