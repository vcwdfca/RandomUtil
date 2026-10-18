package com.vcwdfca.randomutil.network;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class RUNetwork {

    private static final String CHANNEL_NAME = "randomutil";

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);

    private static int nextDiscriminator;

    private RUNetwork() {}

    public static void init() {}

    public static <REQUEST extends IMessage, REPLY extends IMessage> void registerMessage(
        Class<? extends IMessageHandler<REQUEST, REPLY>> handler, Class<REQUEST> message, Side side) {
        CHANNEL.registerMessage(handler, message, nextDiscriminator++, side);
    }

    public static void sendToServer(IMessage message) {
        CHANNEL.sendToServer(message);
    }

    public static void sendTo(EntityPlayerMP player, IMessage message) {
        CHANNEL.sendTo(message, player);
    }
}
