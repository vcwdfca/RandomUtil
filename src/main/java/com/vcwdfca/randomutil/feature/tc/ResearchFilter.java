package com.vcwdfca.randomutil.feature.tc;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;

public final class ResearchFilter {

    private ResearchFilter() {}

    public static ResearchSnapshot compute(ObjectCollection<String> completed, Chapter chapter, String target,
        boolean includeHidden, boolean onlyAvailable) {
        ObjectSet<String> done = new ObjectOpenHashSet<>(completed);
        ObjectSet<String> clues = new ObjectOpenHashSet<>();

        for (String key : done) {
            if (key.startsWith("@")) {
                clues.add(key.substring(1));
            }
        }

        ObjectList<ResearchSnapshot.CatStat> cats = new ObjectArrayList<>();
        int totalAll = 0;
        int doneAll = 0;
        int missingAll = 0;
        int hiddenAll = 0;

        for (Map.Entry<String, ResearchCategoryList> category : ResearchCategories.researchCategories.entrySet()) {
            String catKey = category.getKey();
            if (!chapter.isAll() && !catKey.equals(chapter.key())) {
                continue;
            }

            boolean open = !"ELDRITCH".equals(catKey) || done.contains("ELDRITCHMINOR");
            ObjectList<ResearchSnapshot.Entry> entries = new ObjectArrayList<>();
            int total = 0;
            int completedCount = 0;
            int missing = 0;
            int hidden = 0;
            int available = 0;

            for (ResearchItem research : category.getValue().research.values()) {
                if (research.isVirtual() || research.isAutoUnlock()) {
                    continue;
                }

                total++;
                if (done.contains(research.key)) {
                    completedCount++;
                    continue;
                }

                boolean clue = clues.contains(research.key);
                boolean visible = open && (clue || (!research.isHidden() && !research.isLost()
                    && !(research.isConcealed() && !canUnlock(research, done))));

                if (!visible) {
                    hidden++;
                    if (!includeHidden) {
                        continue;
                    }
                }

                boolean unlockable = canUnlock(research, done);
                if (onlyAvailable && !unlockable) {
                    continue;
                }

                entries.add(new ResearchSnapshot.Entry(research.key, flags(research, unlockable, clue)));
                missing++;
                if (unlockable) {
                    available++;
                }
            }

            cats.add(
                new ResearchSnapshot.CatStat(
                    catKey,
                    ResearchCategories.getCategoryName(catKey),
                    total,
                    completedCount,
                    missing,
                    hidden,
                    available,
                    ObjectLists.unmodifiable(entries)));
            totalAll += total;
            doneAll += completedCount;
            missingAll += missing;
            hiddenAll += hidden;
        }

        return new ResearchSnapshot(
            target,
            chapter.key(),
            totalAll,
            doneAll,
            missingAll,
            hiddenAll,
            ObjectLists.unmodifiable(cats));
    }

    public static boolean canUnlock(ResearchItem research, ObjectSet<String> done) {
        if (research.parents != null) {
            for (String parent : research.parents) {
                if (parent != null && !done.contains(parent)) {
                    return false;
                }
            }
        }

        if (research.parentsHidden != null) {
            for (String parent : research.parentsHidden) {
                if (parent != null && !done.contains(parent)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static int flags(ResearchItem research, boolean unlockable, boolean clue) {
        int flags = 0;
        if (unlockable) {
            flags |= ResearchSnapshot.AVAILABLE;
        }
        if (research.isHidden()) {
            flags |= ResearchSnapshot.HIDDEN;
        }
        if (research.isLost()) {
            flags |= ResearchSnapshot.LOST;
        }
        if (research.isConcealed()) {
            flags |= ResearchSnapshot.CONCEALED;
        }
        if (research.isSecondary()) {
            flags |= ResearchSnapshot.SECONDARY;
        }
        if (research.isSpecial()) {
            flags |= ResearchSnapshot.SPECIAL;
        }
        if (research.isStub()) {
            flags |= ResearchSnapshot.STUB;
        }
        if (clue) {
            flags |= ResearchSnapshot.CLUE;
        }
        return flags;
    }
}
