package com.minemonitor.monitor.server.system;

public interface ISystemMonitor {

    double getCpuUsage();
    long getTotalMemory();
    long getUsedMemory();
    long getFreeMemory();
    long getMaxMemory();

}
