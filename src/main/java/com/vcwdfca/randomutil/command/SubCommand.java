package com.vcwdfca.randomutil.command;

import net.minecraft.command.ICommandSender;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

public interface SubCommand {

    String name();

    default ObjectList<String> aliases() {
        return ObjectLists.emptyList();
    }

    String descriptionKey();

    default ObjectList<String> usageKeys() {
        return ObjectLists.emptyList();
    }

    void execute(ICommandSender sender, ObjectList<String> args, Opts opts);

    default ObjectList<String> suggest(ICommandSender sender, ObjectList<String> args) {
        return ObjectLists.emptyList();
    }
}
