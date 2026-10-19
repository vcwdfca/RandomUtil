package com.vcwdfca.randomutil.feature.tc;

import it.unimi.dsi.fastutil.objects.ObjectList;

public record ResearchSnapshot(String target, String chapterKey, int total, int done, int missing, int hiddenMissing,
    ObjectList<CatStat> cats) {

    public static final int AVAILABLE = 1;
    public static final int HIDDEN = 2;
    public static final int LOST = 4;
    public static final int CONCEALED = 8;
    public static final int CLUE = 16;
    public static final int SECONDARY = 32;
    public static final int SPECIAL = 64;
    public static final int STUB = 128;

    public record CatStat(String key, String displayName, int total, int done, int missing, int hiddenMissing,
        int available, ObjectList<Entry> entries) {}

    public record Entry(String key, int flags) {

        public boolean has(int flag) {
            return (flags & flag) != 0;
        }
    }
}
