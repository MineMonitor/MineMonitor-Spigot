package com.minemonitor;

import com.minemonitor.config.ConfigManager;
import com.minemonitor.config.connection.ConfigSetup;
import com.minemonitor.connection.ConnectionManager;
import com.minemonitor.connection.IConnectionManager;
import com.minemonitor.message.MessageKey;
import com.minemonitor.monitor.IMonitorManager;
import mcapi.davidout.manager.file.IFileManager;
import mcapi.davidout.manager.file.json.JsonFileManager;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	private SetUp setup;


	private IConnectionManager connectionManager;
	private IMonitorManager statsManager;
	private ConfigManager configManager;
	private IFileManager fileManager;

	@Override
	public void onEnable() {
		instance = this;
		setup = new SetUp(this);

		try {
			initializeManagers();
			Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ON_ENABLE));
			if(!ConfigSetup.getInstance().isInSetup()) {
				getConnectionManager().connect();
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("Error during plugin initialization: " + e.getMessage());
			e.printStackTrace();
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
		statsManager = setup.createStatsManager();
		connectionManager = new ConnectionManager(
				this
		);

	}


	public static Main getInstance() {
		return instance;
	}

	public IConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public IMonitorManager getStatsManager() {
		return statsManager;
	}

	public IFileManager getFileManager() {
		return fileManager;
	}
}
