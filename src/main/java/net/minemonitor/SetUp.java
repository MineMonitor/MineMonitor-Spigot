package net.minemonitor;

import net.minemonitor.api.request.ICallback;
import net.minemonitor.command.CMD_MineMonitor;
import net.minemonitor.event.EditConfig;
import net.minemonitor.event.PlayerJoin;
import net.minemonitor.message.MessageKey;
import net.minemonitor.plugin.Guis;
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
        this.setConnectionCallbacks();
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
        boolean createdFolder = bundleFolder.mkdirs();

        if (!defaultLanguage.exists()) {
            try {
                ResourceUtils.copyFileFromResources(plugin, "en.json", defaultLanguage.getPath());
            } catch (Exception ex) {
                Bukkit.getConsoleSender().sendMessage("Error copying default language file: " + ex.getMessage());
            }
        }

        MessageManager mm = new MessageManager(new JsonFileManager(bundleFolder));
        mm.loadMessageBundles();
        mm.setCurrentBundle("messages");
    }

    public void setConnectionCallbacks() {
        MineMonitorApi.getInstance().getConnectionManager().setConnectCallback(new ICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(!aBoolean) {
                    return;
                }
                Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_CONNECTED));
            }

            @Override
            public void onError(Exception e) {
                Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_COULDNOTCREATECONNECTION));
            }
        });

        MineMonitorApi.getInstance().getConnectionManager().setDisconnectCallback(new ICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(!aBoolean) {
                    return;
                }

                Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_DISCONNECTED));
            }

            @Override
            public void onError(Exception e) {}
        });
    }


}
