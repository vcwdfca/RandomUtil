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

        Thaum.load(configuration);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static final class Thaum {

        public static boolean enableQuery = true;

        public static boolean allowQueryOthers = true;

        public static boolean allowOfflineQuery = false;

        public static boolean requireOpForOffline = true;

        private Thaum() {}

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
}
