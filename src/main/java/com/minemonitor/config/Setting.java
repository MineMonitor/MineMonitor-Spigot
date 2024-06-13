package com.minemonitor.config;

public enum Setting {
    SSL, AUTO_CONNECT, HOSTNAME, PORT;

    @Override
    public String toString() {
        return this.name().toLowerCase().replace("_", " ");
    }
}
