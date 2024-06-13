package com.minemonitor.monitor;

import com.minemonitor.monitor.player.playtime.IPlaytimeManager;
import com.minemonitor.monitor.server.system.ISystemMonitor;
import com.minemonitor.monitor.server.tick.ITickMonitor;

public interface IMonitorManager {

    IPlaytimeManager getPlayTimeManager();
    ISystemMonitor getSystemMonitor();
    ITickMonitor getTickMonitor();

}
