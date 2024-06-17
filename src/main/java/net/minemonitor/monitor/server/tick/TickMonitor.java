package net.minemonitor.monitor.server.tick;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TickMonitor implements ITickMonitor {

    private final TPSMonitor monitor;
    private final Plugin plugin;
    private BukkitTask task;

    public TickMonitor(Plugin plugin) {
        this.plugin = plugin;
        this.monitor = new TPSMonitor(plugin);
    }

    @Override
    public void startTPSMonitor() {
        if(task != null) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskTimer(plugin, monitor, 0L, 1L);
    }

    @Override
    public void startAsyncTPSMonitor() {
        if(task != null) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, monitor, 0L, 1L);
    }

    @Override
    public double getTPS() {
        return this.monitor.getTPS();
    }
}
