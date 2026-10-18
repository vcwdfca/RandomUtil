package com.vcwdfca.randomutil;

import net.minecraftforge.client.ClientCommandHandler;

import com.vcwdfca.randomutil.command.CommandRandomUtil;
import com.vcwdfca.randomutil.feature.Features;
import com.vcwdfca.randomutil.util.ClientTickQueue;

import cpw.mods.fml.common.event.FMLInitializationEvent;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientCommandHandler.instance.registerCommand(CommandRandomUtil.instance);
        new ClientTickQueue().register();
        Features.clientInit();
    }
}
