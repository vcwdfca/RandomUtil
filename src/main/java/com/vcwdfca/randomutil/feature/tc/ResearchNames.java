package com.vcwdfca.randomutil.feature.tc;

import net.minecraft.util.StatCollector;

import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public final class ResearchNames {

    private ResearchNames() {}

    public static String name(String key) {
        ResearchItem research = ResearchCategories.getResearch(key);
        if (research != null) {
            String localized = research.getName();
            if (localized != null && !localized.isEmpty() && !localized.startsWith("tc.research_name.")) {
                return localized;
            }
        }

        String translationKey = "tc.research_name." + key;
        return StatCollector.canTranslate(translationKey) ? StatCollector.translateToLocal(translationKey) : key;
    }
}
