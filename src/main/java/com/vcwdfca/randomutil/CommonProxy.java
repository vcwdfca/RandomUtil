package com.vcwdfca.randomutil;

import com.vcwdfca.randomutil.feature.Features;
import com.vcwdfca.randomutil.network.RUNetwork;
import com.vcwdfca.randomutil.util.ServerTickQueue;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        RandomUtil.LOG.info("RandomUtil " + Tags.VERSION + " loading");
    }

    public void init(FMLInitializationEvent event) {
        RUNetwork.init();
        new ServerTickQueue().register();
        Features.init();
    }
}
