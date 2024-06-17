package net.minemonitor.message;

public enum MessageKey implements mcapi.davidout.manager.language.message.MessageKey {

    ON_ENABLE("onEnable"),
    ON_DISABLE("onDisable"),
    ONLY_PLAYER("onlyPlayer"),
    NO_PERMISSION("noPermission"),
    USE_COMMAND("commandUsage"),
    SETUP_REQUIRED("setup.runSetup"),
    SETUP_FAILED("setup.failed"),
    SETUP_FINISHED("setup.finished"),
    ERROR_LOADCONFIG("error.loadConfig"),
    ERROR_SAVECONFIG("error.saveConfig"),
    ERROR_COPYDEFAULTLANGUAGE("error.copyDefaultLanguage"),

    ERROR_URI("error.uri"),
    ERROR_NOURL("error.noUrl"),
    ERROR_COULDNOTCREATECONNECTION("error.couldNotCreateConnection"),
    CONNECTION_TRYCONNECT("connection.tryConnect"),
    CONNECTION_AUTOCONNECT("connection.autoConnect"),
    CONNECTION_CONNECTED("connection.connected"),
    CONNECTION_DISCONNECTED("connection.disconnected"),

    CONNECTION_NOT_CONNECTED("connection.notConnected"),
    CONNECTION_IS_CONNECTED("connection.isConnected"),
    CONNECTION_SSL("connection.ssl"),
    CONNECTION_EDIT_SETTING("connection.editSetting"),
    CONNECTION_SAVED_SETTING("connection.savedSetting"),

    SETTING_PLAYTIME("setting.playTime"),
    SETTING_TPS("setting.tps"),
    SETTING_SYSTEM_MONITOR("setting.systemMonitor"),

    ENABLED("enabled"),
    DISABLED("disabled"),
    MUST_BE_NUMBER("number"),

    CLICK_COPY("click.copy"),

    AUTHENTICATE("authenticate"),
    WEBSITE_GO_TO("website.goTo")

    ;

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}


