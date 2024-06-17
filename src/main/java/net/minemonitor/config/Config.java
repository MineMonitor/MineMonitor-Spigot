package net.minemonitor.config;

import net.minemonitor.plugin.DataSetting;

import java.util.HashMap;

public class Config {

    public HashMap<String, Boolean> settings;

    public Config(HashMap<String, Boolean> settings) {
        this.settings = settings;
    }

    public Config() {
        this.settings = new HashMap<>();
        for (DataSetting value : DataSetting.values()) {
            this.settings.put(value.toString(), true);
        }
    }

    public boolean isEnabled(DataSetting setting) {
        return settings.get(setting.toString());
    }

    public void setSetting(DataSetting dataSetting, boolean bool) {
        settings.put(dataSetting.toString(), bool);
    }


    public HashMap<DataSetting, Boolean> getDataSettings() {
        HashMap<DataSetting, Boolean> dSettings = new HashMap<>();

        settings.forEach((setting, aBoolean) -> {
            dSettings.put(DataSetting.fromString(setting), aBoolean);
        });
        return dSettings;
    }
}
