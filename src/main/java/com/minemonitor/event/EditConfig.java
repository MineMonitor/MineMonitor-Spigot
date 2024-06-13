package com.minemonitor.event;

import com.minemonitor.Main;
import com.minemonitor.config.ConfigManager;
import com.minemonitor.config.Setting;
import com.minemonitor.config.connection.ConfigSetup;
import com.minemonitor.gui.PluginSettingsGui;
import com.minemonitor.plugin.Guis;
import com.minemonitor.message.MessageKey;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class EditConfig implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Setting setting = getSetting(e.getPlayer().getUniqueId());
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

    public Setting getSetting(UUID uuid) {
        return (ConfigSetup.getInstance().isInSetup()) ?
                ConfigSetup.getInstance().getSettingUserIsEditing(uuid) :
                Main.getInstance().getConfigManager().getSettingUserIsEditing(uuid);
    }

    public void stopEditing(UUID uuid) {
        if(ConfigSetup.getInstance().isInSetup()) {
            ConfigSetup.getInstance().stopEditing(uuid);
            return;
        }

        Main.getInstance().getConfigManager().removeEditing(uuid);
    }

    public void editSettingAndSave(Player p, Setting setting, String value) {
        UUID uuid = p.getUniqueId();


        if(ConfigSetup.getInstance().isInSetup()) {
            switch (setting) {
                case HOSTNAME:
                    ConfigSetup.getInstance().getConfig().hostname = value;
                    break;

                case PORT:
                    int number = getNumber(value);
                    if(number == -1) {
                        p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.MUST_BE_NUMBER));
                        break;
                    }
                        ConfigSetup.getInstance().getConfig().port = number;
                    break;
            }

            ConfigSetup.getInstance().stopEditing(uuid);
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
                    if(number == -1) {
                        p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.MUST_BE_NUMBER));
                        break;
                    }
                    Main.getInstance().getConfigManager().getConnectionConfig().port = number;
                    Main.getInstance().getConfigManager().saveConfig(ConfigManager.ConfigType.CONNECTION_CONFIG);

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


