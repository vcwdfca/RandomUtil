package com.vcwdfca.randomutil.util;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.vcwdfca.randomutil.RandomUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ServerTickQueue {

    private static final ConcurrentLinkedQueue<Runnable> QUEUE = new ConcurrentLinkedQueue<>();

    public static void post(Runnable r) {
        QUEUE.add(r);
    }

    public void register() {
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) {
            return;
        }

        Runnable r;
        while ((r = QUEUE.poll()) != null) {
            try {
                r.run();
            } catch (Throwable t) {
                RandomUtil.LOG.error("server task failed", t);
            }
        }
    }
}
