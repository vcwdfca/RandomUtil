package com.vcwdfca.randomutil.feature.tc;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectList;

public final class TCFacade {

    private TCFacade() {}

    public static Chapter resolveChapter(String input) {
        return Chapters.resolve(input);
    }

    public static ObjectList<String> chapterSuggestions() {
        return Chapters.suggestions();
    }

    public static String chapterName(String key) {
        return Chapters.displayName(key);
    }

    public static ResearchSnapshot snapshot(ObjectCollection<String> completed, Chapter chapter, String target,
        boolean includeHidden, boolean onlyAvailable) {
        return ResearchFilter.compute(completed, chapter, target, includeHidden, onlyAvailable);
    }
}
