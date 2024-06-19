package net.minemonitor.config;

import net.minemonitor.Main;
import mcapi.davidout.manager.file.IFileManager;
import mcapi.davidout.manager.language.MessageManager;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.api.connection.config.ConnectionSetting;
import net.minemonitor.api.connection.config.ConnectionSettings;
import net.minemonitor.api.connection.config.IConnectionSettings;
import org.bukkit.Bukkit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private final HashMap<UUID, ConnectionSetting> editSetting = new HashMap<>();
    private final IFileManager fileManager;
    private IConnectionSettings connection;
    private Config config;

    public ConfigManager(IFileManager fileManager) {
        this.fileManager = fileManager;
        this.loadConfig();
    }

    public IConnectionSettings getConnectionConfig() {
        return this.connection;
    }

    public Config getConfig() {
        return this.config;
    }

    public void loadConfig() {
        loadConfig(ConfigType.PLUGIN_CONFIG);
        loadConfig(ConfigType.CONNECTION_CONFIG);
    }

    private void loadConfig(ConfigType type) {
        try {
            switch (type) {
                case PLUGIN_CONFIG:
                    this.config = fileManager.loadFile(Config.class, "config");
                    break;
                case CONNECTION_CONFIG:
                    this.connection = fileManager.loadFile(ConnectionSettings.class, "connection");
                    if(this.connection.serverId != null) {
                        return;
                    }

                    MineMonitorApi.getInstance().getSetupManager().runSetup(fileManager.getBaseFolder());
                    break;
            }
        } catch (FileNotFoundException e) {
            handleFileNotFoundException(type);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage("error.loadConfig"));
        }
    }

    private void handleFileNotFoundException(ConfigType type) {
        try {
            switch (type) {
                case PLUGIN_CONFIG:
                    this.config = new Config();
                case CONNECTION_CONFIG:
                    MineMonitorApi.getInstance().getSetupManager().runSetup(Main.getInstance().getDataFolder());
                    break;
            }

            this.saveConfig(type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig(ConfigType type) throws IOException {
        switch (type) {
            case PLUGIN_CONFIG:
                fileManager.saveFile(this.config, "config");
                break;

            case CONNECTION_CONFIG:
                fileManager.saveFile(this.connection, "connection");
                break;
        }
    }

    public void putEditSettings(ConnectionSetting settings, UUID uuid) {
        editSetting.put(uuid, settings);
    }

    public void removeEditing(UUID uuid) {
        editSetting.remove(uuid);
    }

    public boolean isEditing(UUID uuid) {
        return editSetting.get(uuid) != null;
    }

    public ConnectionSetting getSettingUserIsEditing(UUID uuid) {
        return editSetting.get(uuid);
    }

    public enum ConfigType {
        PLUGIN_CONFIG, CONNECTION_CONFIG
    }
}
