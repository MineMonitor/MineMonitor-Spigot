package net.minemonitor.gui;

import net.minemonitor.Main;
import net.minemonitor.MineMonitorApi;
import net.minemonitor.interfaces.connection.IConnectionSettings;
import net.minemonitor.interfaces.service.IHubConnectionManager;
import net.minemonitor.message.MessageKey;
import net.minemonitor.plugin.Guis;
import net.minemonitor.plugin.Permissions;
import net.minemonitor.utils.Items;

import mcapi.davidout.manager.gui.IGui;
import mcapi.davidout.manager.language.MessageManager;
import mcapi.davidout.utils.ItemCreator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SettingsGui extends IGui {

    private ItemStack pluginOptions;
    private ItemStack connection;
    private ItemStack connectionSettings;

    @Override
    public String getTitle(Player player, Object... objects) {
        return "&cSettings";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        IHubConnectionManager manager = MineMonitorApi.getInstance().getConnectionManager();
        IConnectionSettings connectionConfig = manager.getConnectionSettings();

        connection = (manager.isConnected()) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&eConnection", Arrays.asList("&7Click to terminate this connection.", " ", "&aConnected")) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&eConnection", Arrays.asList("&7Click to make an connection to the server.", "&cNot Connected")) ;

        pluginOptions = ItemCreator.createItem(Material.BOOK, "&ePlugin Settings", "&7Click to see the plugin settings");
        connectionSettings = ItemCreator.createItem(
                ItemCreator.getTypeItem("LEGACY_BOOK_AND_QUILL", "BOOK_AND_QUILL")
                , "&eConnection Settings",
                Arrays.asList(
                        "&7Hostname: &e" +  connectionConfig.hostname,
                        "&7Port: &e" + connectionConfig.port,
                        "&7Auto reconnect: " + enabled(connectionConfig.autoReconnect),
                        "&7SSL: " + enabled(connectionConfig.SSL)
                )
        );

        inventory.setItem(11,pluginOptions);
        inventory.setItem(13,connection);
        inventory.setItem(15,connectionSettings);
    }

    private String enabled(boolean enabled) {
        return ((enabled) ? "&aenabled" : "&cdisabled");
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

        if(e.getCurrentItem().equals(pluginOptions)) {
            p.closeInventory();
            Guis.PLUGIN_SETTINGS_GUI.openInventory(p);
            return;
        }

        if(e.getCurrentItem().equals(connectionSettings)) {
            Guis.CONNECTION_SETTINGS_GUI.openInventory(p);
            return;
        }

        if(e.getCurrentItem().equals(connection) && p.hasPermission(Permissions.EDIT_CONFIG.getPermission())) {
            if(!p.hasPermission(Permissions.CONNECTION.getPermission())) {
                p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.NO_PERMISSION));
                return;
            }

            if(manager.isConnected()) {
              MineMonitorApi.getInstance().getConnectionManager().disconnect();
                updateInventory(e.getClickedInventory());
                return;
            }

            MineMonitorApi.getInstance().getConnectionManager().connect(Main.getInstance().getConfigManager().getConnectionConfig());
            updateInventory(e.getClickedInventory());
        }

    }
}
