package net.minemonitor.gui;

import net.minemonitor.Main;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.config.ConfigManager;
import net.minemonitor.interfaces.service.IHubConnectionManager;
import net.minemonitor.message.MessageKey;
import net.minemonitor.model.connection.ConnectionSetting;
import net.minemonitor.plugin.Permissions;
import net.minemonitor.utils.Items;
import mcapi.davidout.manager.gui.IGui;
import mcapi.davidout.manager.language.MessageManager;
import mcapi.davidout.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


public class ConnectionSettingsGui extends IGui {

    private ItemStack connected;
    private ItemStack autoReconnect;
    private ItemStack ssl;
    private ItemStack hostname;
    private ItemStack port;

    @Override
    public String getTitle(Player player, Object... objects) {
        return "&cConnection Settings";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        IHubConnectionManager manager = MineMonitorApi.getInstance().getConnectionManager();

        connected = (manager.isConnected()) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&7Connection: ", Collections.singletonList(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_IS_CONNECTED))) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&7Connection: ", Collections.singletonList(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_NOT_CONNECTED)));

        autoReconnect = (manager.autoConnectEnabled()) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&7Auto reconnect: ", Arrays.asList(
                                MessageManager.getInstance().getMessage(MessageKey.CONNECTION_AUTOCONNECT),
                                MessageManager.getInstance().getMessage(MessageKey.ENABLED)
                        )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&7Auto reconnect: ", Arrays.asList(
                                MessageManager.getInstance().getMessage(MessageKey.CONNECTION_AUTOCONNECT),
                                MessageManager.getInstance().getMessage(MessageKey.DISABLED)
                        ));

        ssl = (Main.getInstance().getConfigManager().getConnectionConfig().SSL) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&7SSL: ", Arrays.asList(
                                MessageManager.getInstance().getMessage(MessageKey.CONNECTION_SSL),
                                MessageManager.getInstance().getMessage(MessageKey.ENABLED)
                        )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&7SSL: ", Arrays.asList(
                                MessageManager.getInstance().getMessage(MessageKey.CONNECTION_SSL),
                                MessageManager.getInstance().getMessage(MessageKey.DISABLED)
                        ));


        hostname = ItemCreator.createItem(Material.PAPER, "&7Hostname: ", Collections.singletonList("&e" + Main.getInstance().getConfigManager().getConnectionConfig().hostname));
        port = ItemCreator.createItem(Material.PAPER, "&7Port: ", Collections.singletonList("&e" + Main.getInstance().getConfigManager().getConnectionConfig().port));

        inventory.setItem(11, connected);
        inventory.setItem(12, autoReconnect);
        inventory.setItem(13, ssl);
        inventory.setItem(14, hostname);
        inventory.setItem(15, port);
    }

    @Override
    public Inventory createInventory(Inventory inventory, Player player, Object... objects) {
        fillInventory(inventory, Items.getGlassPane());
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == e.getWhoClicked().getInventory()) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if(!p.hasPermission(Permissions.EDIT_CONFIG.getPermission())) {
            return;
        }

        IHubConnectionManager manager = MineMonitorApi.getInstance().getConnectionManager();

        if(e.getCurrentItem().equals(connected)) {
            boolean connected = manager.isConnected();

            if(connected) {
                MineMonitorApi.getInstance().getConnectionManager().disconnect();
                updateInventory(e.getClickedInventory());

                return;
            }

            MineMonitorApi.getInstance().getConnectionManager().connect(Main.getInstance().getConfigManager().getConnectionConfig());
            updateInventory(e.getClickedInventory());
            return;
        }


        if(e.getCurrentItem().equals(autoReconnect)) {
            Main.getInstance().getConfigManager().getConnectionConfig().autoReconnect =
                    !Main.getInstance().getConfigManager().getConnectionConfig().autoReconnect;
            openInventory((Player) e.getWhoClicked());
            return;
        }

        if(e.getCurrentItem().equals(ssl)) {
            Main.getInstance().getConfigManager().getConnectionConfig().SSL =
            !Main.getInstance().getConfigManager().getConnectionConfig().SSL;
            openInventory((Player) e.getWhoClicked());
            return;
        }

        if(e.getCurrentItem().equals(hostname)) {
            e.getWhoClicked().closeInventory();
            Main.getInstance().getConfigManager().putEditSettings(ConnectionSetting.HOSTNAME, e.getWhoClicked().getUniqueId());
            e.getWhoClicked().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_EDIT_SETTING).replace("%setting", "hostname"));
            return;
        }


        if(e.getCurrentItem().equals(port)) {
            e.getWhoClicked().closeInventory();
            Main.getInstance().getConfigManager().putEditSettings(ConnectionSetting.PORT, e.getWhoClicked().getUniqueId());
            e.getWhoClicked().sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_EDIT_SETTING).replace("%setting", "port"));
        }


    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        try {
            Main.getInstance().getConfigManager().saveConfig(ConfigManager.ConfigType.CONNECTION_CONFIG);
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage(MessageManager.getInstance().getMessage(MessageKey.ERROR_SAVECONFIG));
        }
    }
}
