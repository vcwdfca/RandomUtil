package com.vcwdfca.randomutil.feature.nei;

import com.vcwdfca.randomutil.Config;
import com.vcwdfca.randomutil.feature.Feature;
import com.vcwdfca.randomutil.feature.FeatureDef;
import com.vcwdfca.randomutil.util.Mods;

@FeatureDef
public final class NEIFeature implements Feature {

    @Override
    public String id() {
        return "nei";
    }

    @Override
    public boolean enabled() {
        if (!(Mods.NotEnoughItems.available() && Mods.appliedenergistics2.available())) {
            return false;
        }

        if (!Mods.tunnelPattern) {
            return false;
        }

        return Config.NEI.enableTunnelPatternBookmark;
    }

    @Override
    public void clientInit() {
        NEIIntegration.init();
    }
}
