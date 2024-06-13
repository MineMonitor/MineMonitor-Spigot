package com.minemonitor.monitor.server.tick;

import org.bukkit.plugin.Plugin;

public class TPSMonitor implements Runnable {

    private static final int TICK_COUNT = 1200; // Number of ticks to average over (1200 ticks = 60 seconds)
    private final long[] tickTimes = new long[TICK_COUNT];
    private int tickIndex = 0;
    private final Plugin plugin;

    public TPSMonitor(Plugin plugin) {
        this.plugin = plugin;
        for (int i = 0; i < TICK_COUNT; i++) {
            tickTimes[i] = System.currentTimeMillis();
        }
    }

    @Override
    public void run() {
        tickTimes[tickIndex] = System.currentTimeMillis();
        tickIndex = (tickIndex + 1) % TICK_COUNT;
    }

    public double getTPS() {
        int targetTickIndex = (tickIndex + 1) % TICK_COUNT;
        long elapsed = System.currentTimeMillis() - tickTimes[targetTickIndex];
        double tps = TICK_COUNT / (elapsed / 1000.0);
        return Math.min(20.0, tps);
    }
}
