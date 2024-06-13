package com.minemonitor;

import com.minemonitor.command.CMD_MineMonitor;
import com.minemonitor.event.EditConfig;
import com.minemonitor.event.PlayerJoin;
import com.minemonitor.plugin.Guis;
import com.minemonitor.monitor.IMonitorManager;
import com.minemonitor.monitor.MonitorManager;
import com.minemonitor.monitor.player.playtime.PlaytimeManager;
import com.minemonitor.monitor.server.system.SystemMonitor;
import com.minemonitor.monitor.server.tick.TickMonitor;
import mcapi.davidout.manager.command.CommandManager;
import mcapi.davidout.manager.file.json.JsonFileManager;
import mcapi.davidout.manager.gui.GuiManager;
import mcapi.davidout.manager.language.MessageManager;
import mcapi.davidout.utils.ResourceUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.io.File;

public class SetUp {

    private final CommandManager commandManager;
    private final GuiManager guiManager;

    private final Main plugin;

    public SetUp(Main plugin) {
        this.plugin = plugin;
        this.commandManager = new CommandManager(plugin);
        this.guiManager = new GuiManager();

        this.registerCommands();
        this.registerListeners();
        this.registerGUI();
    }

    public void registerCommands() {
        commandManager.registerCommand(new CMD_MineMonitor());
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(), plugin);
        pm.registerEvents(guiManager, plugin);
        pm.registerEvents(new EditConfig(), plugin);
    }

    public void registerGUI() {
        guiManager.registerGui(Guis.SETTINGS_GUI);
        guiManager.registerGui(Guis.CONNECTION_SETTINGS_GUI);
        guiManager.registerGui(Guis.PLUGIN_SETTINGS_GUI);

        guiManager.registerGui(Guis.SETUP_GUI);
        guiManager.registerGui(Guis.SETUP_CONNECTION_GUI);
    }

    public void createMessageManager() {
        File bundleFolder = new File(plugin.getDataFolder(), "lang");
        File defaultLanguage = new File(bundleFolder, "messages.json");
        bundleFolder.mkdirs();

        if (!defaultLanguage.exists()) {
            try {
                ResourceUtils.copyFileFromResources(plugin, "en.json", defaultLanguage.getPath());
            } catch (Exception ex) {
                Bukkit.getConsoleSender().sendMessage("Error copying default language file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        MessageManager mm = new MessageManager(new JsonFileManager(bundleFolder));
        mm.loadMessageBundles();
        mm.setCurrentBundle("messages");
    }

    public IMonitorManager createStatsManager() {
        return new MonitorManager(
                new PlaytimeManager(),
                new SystemMonitor(),
                new TickMonitor(plugin)
        );
    }

}
