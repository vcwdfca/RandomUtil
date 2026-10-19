package com.vcwdfca.randomutil.network;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;

import com.vcwdfca.randomutil.Config;
import com.vcwdfca.randomutil.feature.tc.Chapter;
import com.vcwdfca.randomutil.feature.tc.ResearchLookup;
import com.vcwdfca.randomutil.feature.tc.ResearchSnapshot;
import com.vcwdfca.randomutil.feature.tc.TCFacade;
import com.vcwdfca.randomutil.util.Mods;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectList;

public final class QueryServer {

    private static final String PERMISSION = "randomutil";

    private static final long COOLDOWN_MS = 1000L;

    private static final Object2LongOpenHashMap<UUID> LAST_QUERY = new Object2LongOpenHashMap<>();

    private QueryServer() {}

    public static void handle(EntityPlayerMP asker, PacketResearchRequest request) {
        if (asker == null) {
            return;
        }

        int id = request.id();
        if (!Config.TC.enableQuery) {
            reply(asker, id, PacketResearchResponse.DISABLED);
            return;
        }

        String self = asker.getCommandSenderName();
        String target = request.target() == null || request.target()
            .isEmpty() ? self : request.target();
        boolean isSelf = target.equalsIgnoreCase(self);

        if (!isSelf) {
            if (!Config.TC.allowQueryOthers) {
                reply(asker, id, PacketResearchResponse.DENIED);
                return;
            }

            if (!ResearchLookup.isOnline(target)) {
                if (!Config.TC.allowOfflineQuery) {
                    reply(asker, id, PacketResearchResponse.NO_DATA);
                    return;
                }

                if (Config.TC.requireOpForOffline && !asker.canCommandSenderUseCommand(2, PERMISSION)) {
                    reply(asker, id, PacketResearchResponse.DENIED);
                    return;
                }
            }

            if (!allowNow(asker)) {
                reply(asker, id, PacketResearchResponse.RATE_LIMITED);
                return;
            }
        }

        if (!Mods.Thaumcraft.available()) {
            reply(asker, id, PacketResearchResponse.NO_THAUMCRAFT);
            return;
        }

        ObjectList<String> completed = ResearchLookup.lookup(target);
        if (completed == null) {
            reply(asker, id, PacketResearchResponse.NO_DATA);
            return;
        }

        Chapter chapter = Chapter.of(request.chapter());
        ResearchSnapshot snapshot = TCFacade
            .snapshot(completed, chapter, target, request.includeHidden(), request.onlyAvailable());
        RUNetwork.sendTo(asker, new PacketResearchResponse(id, PacketResearchResponse.OK, snapshot));
    }

    private static boolean allowNow(EntityPlayerMP player) {
        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueID();
        if (now - LAST_QUERY.getLong(uuid) < COOLDOWN_MS) {
            return false;
        }

        LAST_QUERY.put(uuid, now);
        return true;
    }

    private static void reply(EntityPlayerMP player, int id, byte status) {
        RUNetwork.sendTo(player, new PacketResearchResponse(id, status));
    }
}
