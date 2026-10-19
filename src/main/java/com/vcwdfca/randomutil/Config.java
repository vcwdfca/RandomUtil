package com.vcwdfca.randomutil;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class Config {

    public static boolean debug;

    private Config() {}

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        debug = configuration
            .getBoolean("debug", "general", false, "Print extra diagnostics to the log while the game runs.");

        TC.load(configuration);
        NEI.load(configuration);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static final class TC {

        public static boolean enableQuery = true;

        public static boolean allowQueryOthers = true;

        public static boolean allowOfflineQuery = false;

        public static boolean requireOpForOffline = true;

        private TC() {}

        static void load(Configuration configuration) {
            enableQuery = configuration.getBoolean(
                "enableQuery",
                "thaum",
                enableQuery,
                "Allow clients to ask this server which Thaumcraft research a player is missing.");
            allowQueryOthers = configuration.getBoolean(
                "allowQueryOthers",
                "thaum",
                allowQueryOthers,
                "Allow querying a player other than yourself.");
            allowOfflineQuery = configuration.getBoolean(
                "allowOfflineQuery",
                "thaum",
                allowOfflineQuery,
                "Allow querying offline players by reading their .thaum file from the save.");
            requireOpForOffline = configuration.getBoolean(
                "requireOpForOffline",
                "thaum",
                requireOpForOffline,
                "Only permission level 2 or higher may query offline players.");
        }
    }

    public static final class NEI {

        public static boolean enableTunnelPatternBookmark = true;

        private NEI() {}

        static void load(Configuration configuration) {
            enableTunnelPatternBookmark = configuration.getBoolean(
                "enableTunnelPatternBookmark",
                "nei",
                enableTunnelPatternBookmark,
                "Bookmark AE2 patterns (including tunnel patterns) from the NEI bookmark key and drop them "
                    + "straight into the pattern terminal input grid with a quantity of one.");
        }
    }
}
