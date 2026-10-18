package com.vcwdfca.randomutil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RandomUtil.MODID, version = Tags.VERSION, name = "RandomUtil", acceptedMinecraftVersions = "[1.7.10]")
public class RandomUtil {

    public static final String MODID = "randomutil";
    public static final Logger LOG = LogManager.getLogger(MODID);

    public static boolean debugEnabled() {
        return Config.debug;
    }

    @SidedProxy(clientSide = "com.vcwdfca.randomutil.ClientProxy", serverSide = "com.vcwdfca.randomutil.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
