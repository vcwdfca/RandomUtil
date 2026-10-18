package com.vcwdfca.randomutil.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public final class Chat {

    private Chat() {}

    public static void send(ICommandSender target, String message) {
        if (message != null && !message.isEmpty()) {
            target.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void error(ICommandSender target, String message) {
        send(target, EnumChatFormatting.RED + message);
    }

    public static String tr(String key, Object... args) {
        return args.length == 0 ? StatCollector.translateToLocal(key)
            : StatCollector.translateToLocalFormatted(key, args);
    }
}
