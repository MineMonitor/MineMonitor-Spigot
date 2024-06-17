package net.minemonitor.connection;

import net.minemonitor.message.MessageKey;
import mcapi.davidout.manager.language.MessageManager;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.api.connection.IConnectionManager;
import net.minemonitor.api.connection.IServerConnection;
import net.minemonitor.api.connection.config.IConnectionSettings;
import org.bukkit.Bukkit;

import java.net.URISyntaxException;

public class ApiServerConnection implements IConnectionManager {

    private IConnectionSettings connectionSettings;

    public ApiServerConnection(IConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    public void connectToServer() {

        try {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_TRYCONNECT).replace("%url", getUrl()));
            this.connectToServer(this.connectionSettings);
        } catch (URISyntaxException e) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_URI).replace("%uri", getUrl()));
        }
    }

    @Override
    public IServerConnection connectToServer(IConnectionSettings iConnectionSettings) throws RuntimeException, URISyntaxException {
        IServerConnection connection = MineMonitorApi.getInstance().getConnectionManager().connectToServer(iConnectionSettings);
        if(connection == null) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_COULDNOTCREATECONNECTION));
            return null;
        }

        Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_CONNECTED));
        return connection;
    }

    @Override
    public boolean disconnectFromServer() {
        Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_DISCONNECTED));
        return MineMonitorApi.getInstance().getConnectionManager().disconnectFromServer();
    }

    @Override
    public boolean isConnected() {
        return MineMonitorApi.getInstance().getConnectionManager().isConnected();
    }

    @Override
    public boolean autoReconnect() {
        return MineMonitorApi.getInstance().getConnectionManager().autoReconnect();
    }

    @Override
    public void setAutoReconnect(boolean b) {
        MineMonitorApi.getInstance().getConnectionManager().setAutoReconnect(b);
    }

    @Override
    public String getUrl() {
        return MineMonitorApi.getInstance().getConnectionManager().getUrl();
    }
}
