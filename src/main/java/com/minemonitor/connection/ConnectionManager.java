package com.minemonitor.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.minemonitor.Main;
import com.minemonitor.config.connection.ConnectionConfig;
import com.minemonitor.connection.transfer.DataTransferObject;
import com.minemonitor.message.MessageKey;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.Bukkit;

import java.net.URI;
import java.net.URISyntaxException;

public class ConnectionManager implements IConnectionManager {

    private MineMonitorClient client;
    private final Main plugin;
    private String url;

    public ConnectionManager(Main plugin) {
        this.plugin = plugin;
    }


    @Override
    public void connect() {
        try {
            this.url = getURI(plugin.getConfigManager().getConnectionConfig());
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_TRYCONNECT).replace("%url", url));
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_URI).replace("%uri", ""));
        }

        if(url == null) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_COULDNOTCREATECONNECTION));
            return;
        }

        try {
            this.client = new MineMonitorClient(this);
            this.client.connect();
        } catch (URISyntaxException e) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_URI).replace("%uri", url));
        }
    }

    @Override
    public void disconnect() {
        if(client != null) {
            this.client.close();
        }

    }

    @Override
    public boolean isConnected() {
        if(client == null || client.isClosed() || !client.isOpen()) {
            return false;
        }

        return client.isOpen();
    }

    @Override
    public boolean autoReconnect() {
        return plugin.getConfigManager().getConnectionConfig().autoReconnect;
    }

    @Override
    public void checkForReconnect() {
        if(!autoReconnect()) {
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::connect, 50);
    }

    @Override
    public void sendData(String message) {
        if(!isConnected()) {
            return;
        }

        client.send(message);
    }


    @Override
    public void sendData(DataTransferObject object) throws JsonProcessingException {
        this.sendData(object.toJson());
    }

    @Override
    public void sendDataAsync(String message) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            this.sendData(message);
        });
    }

    @Override
    public void sendDataAsync(DataTransferObject object) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                this.sendDataAsync(object.toJson());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public URI getUri() throws URISyntaxException {
        return new URI(url);
    }

    public String getURI(ConnectionConfig config) {
        String protocol = config.SSL ? "wss" : "ws";
        return String.format("%s://%s:%d", protocol, config.hostname, config.port);
    }
}
