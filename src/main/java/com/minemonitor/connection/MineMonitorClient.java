package com.minemonitor.connection;

import com.minemonitor.message.MessageKey;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

public class MineMonitorClient extends WebSocketClient {


    private final IConnectionManager connectionManager;

    public MineMonitorClient(IConnectionManager connectionManager) throws URISyntaxException {
        super(connectionManager.getUri());
        this.connectionManager = connectionManager;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_CONNECTED));
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_DISCONNECTED));
        connectionManager.checkForReconnect();
    }

    @Override
    public void onError(Exception e) {
        if(e instanceof ConnectException) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_COULDNOTCREATECONNECTION));
        }
    }



}
