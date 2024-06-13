package com.minemonitor.plugin;

import com.minemonitor.message.MessageKey;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.ChatColor;

public enum DataSetting {
    PLAYTIME(MessageManager.getInstance().getMessage(MessageKey.SETTING_PLAYTIME)),
    TPS(MessageManager.getInstance().getMessage(MessageKey.SETTING_TPS)),
    SYSTEM_MONITOR(MessageManager.getInstance().getMessage(MessageKey.SETTING_SYSTEM_MONITOR))

    ;

    private final String description;

    DataSetting(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String[] words = this.name().toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        sb.append(words[0]);

        for (int i = 1; i < words.length; i++) {
                sb.append(words[i].substring(0, 1).toUpperCase());
                sb.append(words[i].substring(1));
            }

        return sb.toString();
    }

    public static DataSetting fromString(String s) {
        s = ChatColor.stripColor(s);

        StringBuilder sb = new StringBuilder(s);

        for (int i = 1; i < sb.length(); i++) {
            if (Character.isUpperCase(sb.charAt(i))) {
                sb.insert(i, '_');
                i++;
            }
        }

        return DataSetting.valueOf(sb.toString().toUpperCase());
    }

    public static DataSetting fromTitle(String title) {
        if(title == null || title.equalsIgnoreCase("")) {
            return null;
        }

        title = ChatColor.stripColor(title);

        StringBuilder sb = new StringBuilder(title);
        for (int i = 1; i < sb.length(); i++) {
            if (Character.isUpperCase(sb.charAt(i))) {
                sb.insert(i, '_');
                i++;
            }
        }

        return DataSetting.valueOf(sb.toString().toUpperCase());
    }


    public String getTitle() {
        String[] words = this.name().toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase());
            sb.append(word.substring(1));
        }

        return sb.toString();
    }

    public String getDescription() {
        return this.description;
    }
}
