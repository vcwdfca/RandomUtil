package com.vcwdfca.randomutil.feature.tc;

import java.util.Map;

import net.minecraft.util.StatCollector;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import thaumcraft.api.research.ResearchCategories;

public final class Chapters {

    private static final String ALL_KEY = "randomutil.thaum.allChapter";

    private static final Map<String, String[]> ALIASES = new Object2ObjectOpenHashMap<>();

    static {
        ALIASES.put("BASICS", new String[] { "基础", "基础学", "基本信息", "basics", "basic" });
        ALIASES.put("THAUMATURGY", new String[] { "神秘", "神秘学", "thaumaturgy" });
        ALIASES.put("ALCHEMY", new String[] { "炼金", "炼金术", "alchemy" });
        ALIASES.put("ARTIFICE", new String[] { "炼化", "炼化学", "奇术", "artifice" });
        ALIASES.put("GOLEMANCY", new String[] { "傀儡", "傀儡学", "golemancy" });
        ALIASES.put("ELDRITCH", new String[] { "邪术", "邪术学", "eldritch" });
    }

    private Chapters() {}

    public static Chapter resolve(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        if (input.equalsIgnoreCase("all") || "*".equals(input)) {
            return Chapter.ALL;
        }

        for (String key : ResearchCategories.researchCategories.keySet()) {
            if (key.equalsIgnoreCase(input)) {
                return Chapter.of(key);
            }
        }

        for (String key : ResearchCategories.researchCategories.keySet()) {
            String name = ResearchCategories.getCategoryName(key);
            if (name != null && name.equalsIgnoreCase(input)) {
                return Chapter.of(key);
            }
        }

        for (String key : ResearchCategories.researchCategories.keySet()) {
            for (String alias : ALIASES.getOrDefault(key, new String[0])) {
                if (alias.equalsIgnoreCase(input)) {
                    return Chapter.of(key);
                }
            }
        }

        return null;
    }

    public static ObjectList<String> suggestions() {
        ObjectList<String> out = new ObjectArrayList<>();
        out.add("all");

        for (String key : ResearchCategories.researchCategories.keySet()) {
            out.add(key);
            String name = ResearchCategories.getCategoryName(key);
            if (name != null && !name.contains(" ") && !out.contains(name)) {
                out.add(name);
            }
        }

        return out;
    }

    public static String displayName(String key) {
        if (key == null || "*".equals(key)) {
            return StatCollector.translateToLocal(ALL_KEY);
        }

        String name = ResearchCategories.getCategoryName(key);
        return name == null ? key : name;
    }
}
