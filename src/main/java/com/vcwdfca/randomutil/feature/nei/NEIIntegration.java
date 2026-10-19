package com.vcwdfca.randomutil.feature.nei;

import com.vcwdfca.randomutil.RandomUtil;

import codechicken.nei.api.API;
import codechicken.nei.guihook.GuiContainerManager;

final class NEIIntegration {

    static final String KEY_BOOKMARK = "bookmark.add";

    private static boolean initialized;

    private NEIIntegration() {}

    static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        try {
            GuiContainerManager.addInputHandler(PatternBookmarkKeyHandler.INSTANCE);
            API.registerNEIGuiHandler(PatternSlotDropHandler.INSTANCE);
        } catch (Throwable t) {
            RandomUtil.LOG.error("Failed to hook the NEI bookmark integration", t);
            return;
        }

        RandomUtil.LOG.info("NEI tunnel pattern bookmark enabled");
    }
}
