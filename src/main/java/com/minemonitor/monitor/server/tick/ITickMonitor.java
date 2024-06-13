package com.minemonitor.monitor.server.tick;

public interface ITickMonitor {

    void startTPSMonitor();
    void startAsyncTPSMonitor();
    double getTPS();

}
