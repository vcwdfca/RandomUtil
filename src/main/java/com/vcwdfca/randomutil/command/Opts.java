package com.vcwdfca.randomutil.command;

import java.util.Locale;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

public record Opts(ObjectList<String> args, boolean includeHidden, boolean onlyAvailable, boolean raw,
    boolean countOnly, boolean dump, boolean help, int page, int limit, String unknownFlag) {

    public static Opts parse(String[] rawArgs) {
        ObjectList<String> positional = new ObjectArrayList<>();
        boolean includeHidden = false;
        boolean onlyAvailable = false;
        boolean raw = false;
        boolean countOnly = false;
        boolean dump = false;
        boolean help = false;
        int page = 1;
        int limit = 8;
        String unknown = null;

        for (String argument : rawArgs) {
            if (argument == null) {
                continue;
            }

            if (!argument.startsWith("--")) {
                positional.add(argument);
                continue;
            }

            String name = argument.substring(2);
            String value = null;
            int separator = name.indexOf('=');
            if (separator >= 0) {
                value = name.substring(separator + 1);
                name = name.substring(0, separator);
            }

            switch (name.toLowerCase(Locale.ROOT)) {
                case "hidden" -> includeHidden = true;
                case "avail" -> onlyAvailable = true;
                case "raw" -> raw = true;
                case "count" -> countOnly = true;
                case "dump" -> dump = true;
                case "help", "?" -> help = true;
                case "page" -> page = Math.max(1, parseInt(value, 1));
                case "limit" -> limit = Math.clamp(parseInt(value, 8), 1, 50);
                default -> unknown = name;
            }
        }

        return new Opts(
            ObjectLists.unmodifiable(positional),
            includeHidden,
            onlyAvailable,
            raw,
            countOnly,
            dump,
            help,
            page,
            limit,
            unknown);
    }

    private static int parseInt(String value, int fallback) {
        if (value == null || value.isEmpty()) {
            return fallback;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }
}
