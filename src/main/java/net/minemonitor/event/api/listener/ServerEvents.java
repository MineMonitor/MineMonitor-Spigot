package net.minemonitor.event.api.listener;

import mcapi.davidout.manager.language.MessageManager;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.enums.Methods;
import net.minemonitor.message.MessageKey;
import net.minemonitor.model.event.EventListener;
import net.minemonitor.model.event.client.RequestAccessEvent;
import org.bukkit.Bukkit;

public class ServerEvents extends EventListener {

    public void onConnect(RequestAccessEvent e) {
        String token = MineMonitorApi.getInstance().getConnectionManager().getConnectionSettings().token;
        Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.REQUESTED_ACCESS).replace("%connection", e.getConnectionId()));

        if(!e.getPassword().equals(token)) {
            MineMonitorApi.getInstance().getConnectionManager().sendData(Methods.DENIE_ACCESS.getMethod(), e.getConnectionId());
            return;
        }

        MineMonitorApi.getInstance().getConnectionManager().sendData(Methods.GRANT_ACCESS.getMethod(), e.getConnectionId());
    }
}
