package com.vcwdfca.randomutil.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class CommandRandomUtil extends CommandBase {

    private CommandRandomUtil() {}

    public static final CommandRandomUtil instance = new CommandRandomUtil();

    @Override
    public String getCommandName() {
        return "randomutil";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("ru");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return Chat.tr("randomutil.command.usage");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] rawArgs) {
        Opts opts = Opts.parse(rawArgs);

        if (opts.unknownFlag() != null) {
            Chat.error(sender, Chat.tr("randomutil.command.unknownOption", opts.unknownFlag()));
            return;
        }

        if (opts.help() || opts.args()
            .isEmpty()) {
            sendHelp(sender);
            return;
        }

        SubCommand command = SubCommands.find(
            opts.args()
                .getFirst());
        if (command == null) {
            Chat.error(
                sender,
                Chat.tr(
                    "randomutil.command.unknown",
                    opts.args()
                        .getFirst()));
            sendHelp(sender);
            return;
        }

        command.execute(
            sender,
            opts.args()
                .subList(
                    1,
                    opts.args()
                        .size()),
            opts);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(
                args,
                SubCommands.names()
                    .toArray(new String[0]));
        }

        SubCommand command = SubCommands.find(args[0]);
        if (command == null) {
            return null;
        }

        ObjectList<String> rest = new ObjectArrayList<>(args.length - 1);
        rest.addAll(
            Arrays.asList(args)
                .subList(1, args.length));

        return getListOfStringsMatchingLastWord(
            args,
            command.suggest(sender, rest)
                .toArray(new String[0]));
    }

    private static void sendHelp(ICommandSender sender) {
        Chat.send(sender, Chat.tr("randomutil.command.help.header"));

        for (SubCommand command : SubCommands.all()) {
            Chat.send(
                sender,
                Chat.tr("randomutil.command.help.entry", command.name(), Chat.tr(command.descriptionKey())));
            for (String usageKey : command.usageKeys()) {
                Chat.send(sender, Chat.tr(usageKey));
            }
        }
    }
}
