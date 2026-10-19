package com.vcwdfca.randomutil.feature;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Comparator;

import com.vcwdfca.randomutil.RandomUtil;

import cpw.mods.fml.common.discovery.ASMDataTable;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public final class Features {

    private static final ObjectList<Feature> REGISTERED = new ObjectArrayList<>();
    private static final Object2ObjectMap<Class<?>, Feature> BY_CLASS = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<String, Feature> BY_ID = new Object2ObjectOpenHashMap<>();

    private static boolean initialized;
    private static boolean discovered;

    private Features() {}

    public static void register(Feature feature) {
        Feature existing = BY_CLASS.putIfAbsent(feature.getClass(), feature);
        if (existing != null) {
            RandomUtil.LOG.warn(
                "Duplicate feature class {}, ignored",
                feature.getClass()
                    .getName());
            return;
        }

        Feature sameId = BY_ID.putIfAbsent(feature.id(), feature);
        if (sameId != null) {
            BY_CLASS.remove(feature.getClass());
            RandomUtil.LOG.warn(
                "Duplicate feature id '{}' ({} vs {})",
                feature.id(),
                sameId.getClass()
                    .getName(),
                feature.getClass()
                    .getName());
            return;
        }
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

    public static void discover(ASMDataTable table) {
        if (discovered) {
            return;
        }
        discovered = true;

        ObjectList<ASMDataTable.ASMData> candidates = new ObjectArrayList<>();
        for (ASMDataTable.ASMData data : table.getAll(FeatureDef.class.getName())) {
            if (data.getClassName()
                .startsWith("com.vcwdfca.randomutil.feature.")) {
                candidates.add(data);
            }
        }
        candidates.sort(Comparator.comparing(ASMDataTable.ASMData::getClassName));

        for (ASMDataTable.ASMData data : candidates) {
            String name = data.getClassName();
            try {
                Class<?> type = Class.forName(name, false, Features.class.getClassLoader());
                if (!Feature.class.isAssignableFrom(type) || type.isInterface()
                    || Modifier.isAbstract(type.getModifiers())
                    || type.isMemberClass()) {
                    RandomUtil.LOG.warn("@FeatureDef on unsupported class {}", name);
                    continue;
                }
                Constructor<?> constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                Feature feat = (Feature) constructor.newInstance();
                if (!feat.enabled()) {
                    continue;
                }
                register(feat);
            } catch (Throwable t) {
                RandomUtil.LOG.error("Failed to create feature {}", name, t);
            }
        }
        if (candidates.isEmpty()) {
            RandomUtil.LOG.warn("@FeatureDef found no feature classes - check the mod container");
        } else if (RandomUtil.debugEnabled()) {
            RandomUtil.LOG
                .info("Feature discovery: {} candidate(s), {} registered", candidates.size(), REGISTERED.size());
        }
    }
}
