package com.vcwdfca.randomutil.util;

import cpw.mods.fml.common.Loader;

public enum Mods {

    Thaumcraft,
    NotEnoughItems,
    appliedenergistics2;

    public boolean available() {
        return Loader.isModLoaded(this.name());
    }
}
