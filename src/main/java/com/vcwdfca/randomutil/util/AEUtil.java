package com.vcwdfca.randomutil.util;

public final class AEUtil {

    public static boolean findTunnelPatternClass() {
        try {
            Class.forName("appeng.items.misc.ItemTunnelPattern");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
