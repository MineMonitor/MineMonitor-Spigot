package com.minemonitor.plugin;

public enum Permissions {

    ALL("minemonitor.*"),
    SETUP("minemonitor.setup"),
    EDIT_CONFIG("minemonitor.config.edit"),
    SEE_CONFIG("minemonitor.config.see"),
    AUTHENTICATE("minemonitor.authenticate"),
    CONNECTION("minemonitor.connection")

    ;

    private final String permission;

    Permissions(String s) {
        this.permission = s;
    }

    public String getPermission() {
        return permission;
    }
}
