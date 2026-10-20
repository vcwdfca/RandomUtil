package com.vcwdfca.randomutil.util;

import cpw.mods.fml.common.Loader;

public enum Mods {

    Thaumcraft,
    NotEnoughItems,
    appliedenergistics2,
    ae2thing;

    public static final boolean tunnelPattern = findClass("appeng.items.misc.ItemTunnelPattern");

    public boolean available() {
        return Loader.isModLoaded(this.name());
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean findClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
