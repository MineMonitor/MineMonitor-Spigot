package com.minemonitor.config;

import com.minemonitor.config.connection.ConfigSetup;
import com.minemonitor.config.connection.ConnectionConfig;
import mcapi.davidout.manager.file.IFileManager;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.Bukkit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private final HashMap<UUID, Setting> editSetting = new HashMap<>();
    private final IFileManager fileManager;
    private ConnectionConfig connection;
    private Config config;

    public ConfigManager(IFileManager fileManager) {
        this.fileManager = fileManager;
        this.loadConfig();
    }

    public ConnectionConfig getConnectionConfig() {
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
                    this.connection = fileManager.loadFile(ConnectionConfig.class, "connection");

                    if(this.connection.serverId != null) {
                        return;
                    }

                    ConfigSetup.getInstance().runSetup();
                    break;
            }
        } catch (FileNotFoundException e) {
            handleFileNotFoundException(type);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage("error.loadConfig"));
            e.printStackTrace();
        }
    }

    private void handleFileNotFoundException(ConfigType type) {
        try {
            switch (type) {
                case PLUGIN_CONFIG:
                    this.config = new Config();
                case CONNECTION_CONFIG:
                    ConfigSetup.getInstance().runSetup();
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

    public void setConnectionConfig(ConnectionConfig config) {
        if(config.serverId == null) {
            config.serverId = this.connection.serverId;
        }

        this.connection = config;
    }

    public void putEditSettings(Setting settings, UUID uuid) {
        editSetting.put(uuid, settings);
    }

    public void removeEditing(UUID uuid) {
        editSetting.remove(uuid);
    }

    public boolean isEditing(UUID uuid) {
        return editSetting.get(uuid) != null;
    }

    public Setting getSettingUserIsEditing(UUID uuid) {
        return editSetting.get(uuid);
    }

    public enum ConfigType {
        PLUGIN_CONFIG, CONNECTION_CONFIG
    }
}
