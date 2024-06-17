package net.minemonitor;

import net.minemonitor.config.ConfigManager;
import net.minemonitor.connection.ApiServerConnection;
import net.minemonitor.message.MessageKey;
import mcapi.davidout.manager.file.IFileManager;
import mcapi.davidout.manager.file.json.JsonFileManager;
import mcapi.davidout.manager.language.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	private SetUp setup;


	private ApiServerConnection connectionManager;
	private ConfigManager configManager;
	private IFileManager fileManager;

	@Override
	public void onEnable() {
		instance = this;
		setup = new SetUp(this);

		try {
			initializeManagers();
			Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ON_ENABLE));
			if(!MineMonitorApi.getInstance().getSetupManager().isInSetup()) {
				this.connectionManager.connectToServer(this.configManager.getConnectionConfig());
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
		connectionManager = new ApiServerConnection(
				this.configManager.getConnectionConfig()
		);

	}


	public static Main getInstance() {
		return instance;
	}

	public ApiServerConnection getConnectionManager() {
		return connectionManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public IFileManager getFileManager() {
		return fileManager;
	}
}
