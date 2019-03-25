package com.github.old_horizon.jvm.monitor;

import java.util.Optional;

class Config {

    private static final String PREFIX = "jvm.monitor.";

    private final String namespace;
    private final Integer period;

    Config() {
        namespace = System.getProperty(PREFIX + "namespace");
        period = Integer.getInteger(PREFIX + "period");
    }

    Optional<String> getNamespace() {
        return Optional.ofNullable(namespace);
    }

    int getPeriod() {
        return Optional.ofNullable(period).orElse(5);
    }

}
