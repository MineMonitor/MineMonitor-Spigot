package com.minemonitor.config.connection;

import com.minemonitor.Main;
import com.minemonitor.config.ConfigManager;
import com.minemonitor.config.Setting;
import com.minemonitor.connection.request.ICallback;
import com.minemonitor.connection.request.Promise;
import com.minemonitor.connection.request.RequestManager;
import com.minemonitor.connection.request.RequestType;
import com.minemonitor.connection.transfer.response.ServerRegisterDTO;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ConfigSetup {

    private static ConfigSetup instance;
    private HashMap<UUID, Setting> editingSetting;
    private boolean acceptedPrivacy;
    private boolean acceptedTerms;
    private ConnectionConfig config;
    private boolean inSetup;


    public ConfigSetup() {}

    public void runSetup() {
        this.acceptedPrivacy = false;
        this.acceptedTerms = false;
        this.config = new ConnectionConfig();
        this.inSetup = false;
        this.editingSetting = new HashMap<>();
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }

    public void setAcceptedPrivacy(boolean acceptedPrivacy) {
        this.acceptedPrivacy = acceptedPrivacy;
    }

    public boolean acceptedPrivacy() {
        return this.acceptedPrivacy;
    }

    public boolean acceptedTerms() {
        return this.acceptedTerms;
    }

    public boolean isInSetup() {
        return inSetup;
    }

    public boolean readyToSave() {
        return this.acceptedTerms && this.acceptedPrivacy;
    }

    public boolean isEditingSetting(UUID uuid) {
        return editingSetting.get(uuid) != null;
    }

    public void setEditing(UUID uuid, Setting setting) {
        this.editingSetting.put(uuid, setting);
    }

    public void stopEditing(UUID uuid) {
        this.editingSetting.remove(uuid);
    }

    public Setting getSettingUserIsEditing(UUID uuid) {
        return this.editingSetting.get(uuid);
    }

    public void finishSetup(ICallback<ServerRegisterDTO> callback){
        this.inSetup = false;

        String urlPrefix = (getConfig().SSL) ? "https://" : "http://";
        String url = urlPrefix + getConfig().hostname + ":" + getConfig().port + "/server/register";
        Promise<ServerRegisterDTO> promise = RequestManager.sendRequest(RequestType.POST, url, null, ServerRegisterDTO.class);
        promise.whenFinished(serverRegisterDTO -> {
            try {
                if(Main.getInstance().getConfigManager().getConnectionConfig() == null || Main.getInstance().getConfigManager().getConnectionConfig().serverId == null) {
                    this.config.serverId = serverRegisterDTO.uuid;
                }

                Main.getInstance().getConfigManager().setConnectionConfig(this.config);
                Main.getInstance().getConfigManager().saveConfig(ConfigManager.ConfigType.CONNECTION_CONFIG);
                callback.onSucces(serverRegisterDTO);
            } catch (IOException e) {
                callback.onError(e);
            }
        });

        promise.setErrorCallback(callback::onError);
    }

    public ConnectionConfig getConfig() {
        return this.config;
    }


    public static ConfigSetup getInstance() {
        if(instance == null) {
            instance = new ConfigSetup();
        }

        return instance;
    }



}
