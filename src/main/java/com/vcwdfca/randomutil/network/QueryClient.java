package com.vcwdfca.randomutil.network;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.command.ICommandSender;

import com.vcwdfca.randomutil.command.Chat;
import com.vcwdfca.randomutil.command.Opts;
import com.vcwdfca.randomutil.feature.thaum.Chapter;
import com.vcwdfca.randomutil.feature.thaum.ResearchRenderer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

@SideOnly(Side.CLIENT)
public final class QueryClient {

    private static final long TIMEOUT_MS = 3000L;

    private static final Int2ObjectOpenHashMap<Pending> PENDING = new Int2ObjectOpenHashMap<>();

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private QueryClient() {}

    public static void register() {
        FMLCommonHandler.instance()
            .bus()
            .register(new Ticker());
    }

    public static void request(ICommandSender sender, String target, Chapter chapter, Opts opts) {
        int id = NEXT_ID.getAndIncrement();
        PENDING.put(id, new Pending(sender, target, chapter, opts, System.currentTimeMillis()));
        RUNetwork.sendToServer(
            new PacketResearchRequest(id, target, chapter.key(), opts.includeHidden(), opts.onlyAvailable()));
    }

    public static void onResponse(PacketResearchResponse response) {
        Pending pending = PENDING.remove(response.id());
        if (pending == null) {
            return;
        }

        if (response.status() != PacketResearchResponse.OK || response.snapshot() == null) {
            Chat.error(pending.sender(), statusMessage(response.status(), pending.target()));
            return;
        }

        ResearchRenderer.render(pending.sender(), response.snapshot(), pending.opts());
    }

    private static String statusMessage(byte status, String target) {
        return switch (status) {
            case PacketResearchResponse.DENIED -> Chat.tr("randomutil.thaum.msg.queryDenied");
            case PacketResearchResponse.NO_DATA -> Chat.tr("randomutil.thaum.msg.noData", target);
            case PacketResearchResponse.DISABLED -> Chat.tr("randomutil.thaum.msg.queryDisabled");
            case PacketResearchResponse.NO_THAUMCRAFT -> Chat.tr("randomutil.thaum.msg.serverNoThaumcraft");
            case PacketResearchResponse.RATE_LIMITED -> Chat.tr("randomutil.thaum.msg.rateLimited");
            default -> Chat.tr("randomutil.thaum.msg.queryFailed");
        };
    }

    private record Pending(ICommandSender sender, String target, Chapter chapter, Opts opts, long started) {}

    private static final class Ticker {

        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END || PENDING.isEmpty()) {
                return;
            }

            long now = System.currentTimeMillis();
            IntList expired = new IntArrayList();
            for (Int2ObjectMap.Entry<Pending> entry : PENDING.int2ObjectEntrySet()) {
                if (now - entry.getValue()
                    .started() > TIMEOUT_MS) {
                    expired.add(entry.getIntKey());
                }
            }

            for (int i = 0; i < expired.size(); i++) {
                Pending pending = PENDING.remove(expired.getInt(i));
                if (pending != null) {
                    Chat.error(pending.sender(), Chat.tr("randomutil.thaum.msg.timeout"));
                }
            }
        }
    }
}
