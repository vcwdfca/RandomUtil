package com.vcwdfca.randomutil.feature.tc;

import com.vcwdfca.randomutil.command.SubCommands;
import com.vcwdfca.randomutil.feature.Feature;
import com.vcwdfca.randomutil.feature.FeatureDef;
import com.vcwdfca.randomutil.network.QueryClient;

@FeatureDef
public final class TCFeature implements Feature {

    @Override
    public String id() {
        return "thaum";
    }

    @Override
    public void init() {
        SubCommands.register(TCResearchCommand.INSTANCE);
    }

    @Override
    public void clientInit() {
        QueryClient.register();
    }
}
