package com.vcwdfca.randomutil.command;

import java.util.Locale;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class SubCommands {

    private static final Object2ObjectOpenHashMap<String, SubCommand> REGISTRY = new Object2ObjectOpenHashMap<>();

    private SubCommands() {}

    public static void register(SubCommand command) {
        REGISTRY.put(command.name(), command);
        for (String alias : command.aliases()) {
            REGISTRY.put(alias, command);
        }
    }

    public static SubCommand find(String name) {
        return name == null ? null : REGISTRY.get(name.toLowerCase(Locale.ROOT));
    }

    public static ObjectList<SubCommand> all() {
        ObjectOpenHashSet<SubCommand> unique = new ObjectOpenHashSet<>(REGISTRY.values());
        ObjectList<SubCommand> commands = new ObjectArrayList<>(unique);
        commands.sort(
            (first, second) -> first.name()
                .compareToIgnoreCase(second.name()));
        return commands;
    }

    public static ObjectList<String> names() {
        ObjectList<String> names = new ObjectArrayList<>();
        for (SubCommand command : all()) {
            names.add(command.name());
        }
        return names;
    }
}
