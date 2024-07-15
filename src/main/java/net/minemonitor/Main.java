package net.minemonitor;

import net.minemonitor.config.ConfigManager;
import net.minemonitor.interfaces.connection.IConnectionSettings;
import net.minemonitor.message.MessageKey;
import mcapi.davidout.manager.file.IFileManager;
import mcapi.davidout.manager.file.json.JsonFileManager;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	private SetUp setup;


	private ConfigManager configManager;
	private IFileManager fileManager;

	@Override
	public void onEnable() {
		instance = this;
		setup = new SetUp(this);

		try {
			initializeManagers();
			Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ON_ENABLE));
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("Error during plugin initialization: " + e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ON_DISABLE));
	}

	public void initializeManagers() throws Exception {
		setup.createMessageManager();
		fileManager = new JsonFileManager(getDataFolder());
		configManager = new ConfigManager(fileManager);

		if(!MineMonitorApi.getInstance().getSetupManager().isInSetup()) {
			IConnectionSettings settings = getInstance().getConfigManager().getConnectionConfig();
			Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_TRYCONNECT).replace("%url", settings.getWSSUrl() ));
			MineMonitorApi.getInstance().getConnectionManager().connect(settings);
		}
	}








	public static Main getInstance() {
		return instance;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

}
