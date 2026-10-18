package com.vcwdfca.randomutil.feature.thaum;

import com.vcwdfca.randomutil.command.SubCommands;
import com.vcwdfca.randomutil.feature.Feature;
import com.vcwdfca.randomutil.network.PacketResearchRequest;
import com.vcwdfca.randomutil.network.PacketResearchResponse;
import com.vcwdfca.randomutil.network.QueryClient;
import com.vcwdfca.randomutil.network.RUNetwork;

import cpw.mods.fml.relauncher.Side;

public final class ThaumFeature implements Feature {

    @Override
    public String id() {
        return "thaum";
    }

    @Override
    public void init() {
        SubCommands.register(new ThaumResearchCommand());
        RUNetwork.registerMessage(PacketResearchRequest.Handler.class, PacketResearchRequest.class, Side.SERVER);
        RUNetwork.registerMessage(PacketResearchResponse.Handler.class, PacketResearchResponse.class, Side.CLIENT);
    }

    @Override
    public void clientInit() {
        QueryClient.register();
    }
}
