package com.minemonitor.monitor;

import com.minemonitor.monitor.player.playtime.IPlaytimeManager;
import com.minemonitor.monitor.server.system.ISystemMonitor;
import com.minemonitor.monitor.server.tick.ITickMonitor;

public class MonitorManager implements IMonitorManager {

    private final IPlaytimeManager playtimeManager;
    private final ISystemMonitor systemMonitor;
    private final ITickMonitor tickMonitor;

    public MonitorManager(IPlaytimeManager playtimeManager, ISystemMonitor systemMonitor, ITickMonitor tickMonitor) {
        this.playtimeManager = playtimeManager;
        this.systemMonitor = systemMonitor;
        this.tickMonitor = tickMonitor;
    }

    @Override
    public IPlaytimeManager getPlayTimeManager() {
        return playtimeManager;
    }

    @Override
    public ISystemMonitor getSystemMonitor() {
        return systemMonitor;
    }

    @Override
    public ITickMonitor getTickMonitor() {
        return tickMonitor;
    }


}
