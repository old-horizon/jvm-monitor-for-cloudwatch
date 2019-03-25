package com.github.old_horizon.jvm.monitor;

import com.github.old_horizon.jvm.monitor.model.Category;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;

class Stats {

    private static final ClassLoadingMXBean classes = ManagementFactory.getClassLoadingMXBean();
    private static final MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
    private static final ThreadMXBean threads = ManagementFactory.getThreadMXBean();

    private Stats() {
    }

    static List<Category> getCurrentValues() {
        return Arrays.asList(classes(), heap(), threads());
    }

    private static Category classes() {
        return new Category("classes")
                .data("total loaded", classes.getTotalLoadedClassCount())
                .data("loaded", classes.getLoadedClassCount())
                .data("total unloaded", classes.getUnloadedClassCount());
    }

    private static Category heap() {
        final MemoryUsage heap = memory.getHeapMemoryUsage();
        return new Category("heap")
                .bytesData("init", heap.getInit())
                .bytesData("used", heap.getUsed())
                .bytesData("committed", heap.getCommitted())
                .bytesData("max", heap.getMax());
    }

    private static Category threads() {
        return new Category("threads")
                .data("live", threads.getThreadCount())
                .data("live peak", threads.getPeakThreadCount())
                .data("total started", threads.getTotalStartedThreadCount())
                .data("daemon", threads.getDaemonThreadCount());
    }

}
