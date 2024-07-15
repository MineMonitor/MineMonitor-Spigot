package net.minemonitor.event;

import net.minemonitor.Main;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.config.ConfigManager;
import net.minemonitor.model.connection.ConnectionSetting;
import net.minemonitor.plugin.Guis;
import net.minemonitor.message.MessageKey;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class EditConfig implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        ConnectionSetting setting = getSetting(e.getPlayer().getUniqueId());
        UUID uuid = e.getPlayer().getUniqueId();

        if(setting == null) {
            return;
        }

        e.setCancelled(true);

        String message = e.getMessage();
        if(message.equalsIgnoreCase("cancel")) {
           stopEditing(uuid);
            return;
        }

        this.editSettingAndSave(e.getPlayer(), setting, e.getMessage());
    }

    public ConnectionSetting getSetting(UUID uuid) {
        return (MineMonitorApi.getInstance().getSetupManager().isInSetup()) ?
                MineMonitorApi.getInstance().getSetupManager().getSettingUserIsEditing(uuid) :
                Main.getInstance().getConfigManager().getSettingUserIsEditing(uuid);
    }

    public void stopEditing(UUID uuid) {
        if(MineMonitorApi.getInstance().getSetupManager().isInSetup()) {
           MineMonitorApi.getInstance().getSetupManager().stopEditing(uuid);
            return;
        }

        Main.getInstance().getConfigManager().removeEditing(uuid);
    }

    public void editSettingAndSave(Player p, ConnectionSetting setting, String value) {
        UUID uuid = p.getUniqueId();


        if(MineMonitorApi.getInstance().getSetupManager().isInSetup()) {
            switch (setting) {
                case HOSTNAME:
                    MineMonitorApi.getInstance().getSetupManager().getConnectionSettings().hostname = value;
                    break;

                case PORT:
                    int number = getNumber(value);
                    if(number == -1) {
                        p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.MUST_BE_NUMBER));
                        return;
                    }
                    MineMonitorApi.getInstance().getSetupManager().getConnectionSettings().port = number;
                    break;
            }

            MineMonitorApi.getInstance().getSetupManager().stopEditing(uuid);
            p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_SAVED_SETTING));
            Guis.SETUP_CONNECTION_GUI.openInventory(p);
            return;
        }

        try {
            switch (setting) {
                case HOSTNAME:
                    Main.getInstance().getConfigManager().getConnectionConfig().hostname = value;
                    Main.getInstance().getConfigManager().saveConfig(ConfigManager.ConfigType.CONNECTION_CONFIG);
                    break;
                case PORT:
                    int number = getNumber(value);
                    if (number <= 0) {
                        p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.MUST_BE_NUMBER));
                       return;
                    }
                    Main.getInstance().getConfigManager().getConnectionConfig().port = number;
                    Main.getInstance().getConfigManager().saveConfig(ConfigManager.ConfigType.CONNECTION_CONFIG);
                    break;
            }

                Main.getInstance().getConfigManager().removeEditing(uuid);
                p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_SAVED_SETTING));
        } catch (Exception ex) {
            p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_SAVECONFIG));
        }
     }


    public int getNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}


