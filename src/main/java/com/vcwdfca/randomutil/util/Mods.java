package com.vcwdfca.randomutil.util;

import cpw.mods.fml.common.Loader;

public enum Mods {

    Thaumcraft;

    public boolean available() {
        return Loader.isModLoaded(this.name());
    }
}
