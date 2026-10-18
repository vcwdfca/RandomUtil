package com.vcwdfca.randomutil.feature.thaum;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;

import com.google.common.base.Charsets;
import com.vcwdfca.randomutil.RandomUtil;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import thaumcraft.common.Thaumcraft;

public final class ResearchLookup {

    private static final String RESEARCH_TAG = "THAUMCRAFT.RESEARCH";

    private ResearchLookup() {}

    public static ObjectList<String> lookup(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        ArrayList<String> online = Thaumcraft.proxy.getCompletedResearch()
            .get(name);
        if (online != null) {
            return new ObjectArrayList<>(online);
        }

        File file = findFile(name);
        return file == null ? null : read(file);
    }

    public static boolean isOnline(String name) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null || name == null) {
            return false;
        }

        for (var player : server.getConfigurationManager().playerEntityList) {
            if (name.equalsIgnoreCase(player.getCommandSenderName())) {
                return true;
            }
        }

        return false;
    }

    private static File findFile(String name) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return null;
        }

        File world = server.worldServerForDimension(0)
            .getSaveHandler()
            .getWorldDirectory();
        String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8))
            .toString();

        File[] candidates = { new File(world, "players/" + uuid + ".thaum"),
            new File(world, "players/" + name + ".thaum"), new File(world, "playerdata/" + uuid + ".thaum"),
            new File(world, "playerdata/" + name + ".thaum") };

        for (File candidate : candidates) {
            if (candidate.isFile()) {
                return candidate;
            }
        }

        return null;
    }

    private static ObjectList<String> read(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            NBTTagCompound data = CompressedStreamTools.readCompressed(input);
            NBTTagList list = data.getTagList(RESEARCH_TAG, 10);
            ObjectList<String> keys = new ObjectArrayList<>(list.tagCount());

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound entry = list.getCompoundTagAt(i);
                if (entry.hasKey("key")) {
                    keys.add(entry.getString("key"));
                }
            }

            return keys;
        } catch (Exception e) {
            RandomUtil.LOG.warn("Could not read Thaumcraft research data from " + file, e);
            return null;
        }
    }
}
