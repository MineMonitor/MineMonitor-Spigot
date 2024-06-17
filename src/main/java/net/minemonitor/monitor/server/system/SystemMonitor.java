package net.minemonitor.monitor.server.system;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SystemMonitor implements ISystemMonitor {

    private final OperatingSystemMXBean osBean;
    private final Runtime runtime;

    public SystemMonitor() {
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        this.runtime = Runtime.getRuntime();
    }

    @Override
    public double getCpuUsage() {
        if (!(osBean instanceof com.sun.management.OperatingSystemMXBean)) {
            return -1;
        }

        com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
        return sunOsBean.getProcessCpuLoad() * 100;
    }

    @Override
    public long getTotalMemory() {
        return runtime.totalMemory();
    }

    @Override
    public long getUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    @Override
    public long getFreeMemory() {
        return runtime.freeMemory();
    }

    @Override
    public long getMaxMemory() {
        return runtime.maxMemory();
    }
}
