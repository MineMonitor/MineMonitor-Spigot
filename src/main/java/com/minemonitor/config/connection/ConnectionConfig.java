package com.minemonitor.config.connection;

import com.minemonitor.Main;
import com.minemonitor.utils.PasswordGenerator;

import java.util.UUID;

public class ConnectionConfig {

    public boolean autoReconnect;
    public String hostname;
    public UUID serverId;
    public boolean SSL;
    public int port;
    public String token;

    public ConnectionConfig() {
//        this.hostname = "api." + Main.getInstance().getDescription().getWebsite();
        this.hostname = "localhost";
        this.port = 3000;
        this.autoReconnect = true;
        this.SSL = false;
        this.serverId = null;
        this.token = PasswordGenerator.generatePassword(32);
    }
}
