package com.vcwdfca.randomutil.feature;

import com.vcwdfca.randomutil.RandomUtil;
import com.vcwdfca.randomutil.feature.thaum.ThaumFeature;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

public final class Features {

    private static final ObjectArrayList<Feature> REGISTERED = new ObjectArrayList<>();

    private static boolean initialized;

    static {
        register(new ThaumFeature());
    }

    private Features() {}

    public static void register(Feature feature) {
        REGISTERED.add(feature);
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        for (Feature feature : REGISTERED) {
            feature.init();
            if (RandomUtil.debugEnabled()) {
                RandomUtil.LOG.info("Feature enabled: {}", feature.id());
            }
        }
    }

    public static void clientInit() {
        for (Feature feature : REGISTERED) {
            feature.clientInit();
        }
    }

    public static ObjectList<Feature> all() {
        return ObjectLists.unmodifiable(REGISTERED);
    }
}
