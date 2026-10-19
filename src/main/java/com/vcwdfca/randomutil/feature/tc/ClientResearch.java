package com.vcwdfca.randomutil.feature.tc;

import java.util.ArrayList;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;

@SideOnly(Side.CLIENT)
public final class ClientResearch {

    private ClientResearch() {}

    public static ObjectList<String> completedFor(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        Map<String, ArrayList<String>> shared = Thaumcraft.proxy.getCompletedResearch();
        ArrayList<String> completed = shared == null ? null : shared.get(name);
        if (completed == null && GuiResearchBrowser.completedResearch != null) {
            completed = GuiResearchBrowser.completedResearch.get(name);
        }

        return completed == null ? null : new ObjectArrayList<>(completed);
    }
}
