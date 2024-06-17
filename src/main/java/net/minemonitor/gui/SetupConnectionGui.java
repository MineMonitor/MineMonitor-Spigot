package net.minemonitor.gui;

import net.minemonitor.MineMonitorApi;
import net.minemonitor.api.connection.config.ConnectionSetting;
import net.minemonitor.config.setup.ISetupManager;
import net.minemonitor.plugin.Guis;
import net.minemonitor.plugin.Permissions;
import net.minemonitor.message.MessageKey;
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

public class SetupConnectionGui extends IGui  {

    private ItemStack autoreconnect;
    private ItemStack hostname;
    private ItemStack port;
    private ItemStack ssl;

    private ItemStack save;


    @Override
    public String getTitle(Player player, Object... objects) {
         return "&cSetup Connection";
    }

    @Override
    public int getRows() {
        return 3;
    }


    @Override
    public void updateInventory(Inventory inventory) {
        ISetupManager manager = MineMonitorApi.getInstance().getSetupManager();

        autoreconnect = (manager.getConnectionSettings().autoReconnect) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&eAuto reconnect", Arrays.asList(
                        "&7Automaticly reconnect to the server when",
                        "&7the connection is closed.",
                        " ",
                        enabled(manager.getConnectionSettings().autoReconnect)
                )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&eAuto reconnect", Arrays.asList(
                        "&7Automaticly reconnect to the server when",
                        "&7the connection is closed.",
                        " ",
                        enabled(manager.getConnectionSettings().autoReconnect)
                ));

        ssl = (manager.getConnectionSettings().SSL) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&eSSL", Arrays.asList(
                        "&7Encrypt the data when sent",
                        "&7to the server. (Advised)",
                        " ",
                        enabled(manager.getConnectionSettings().SSL)
                )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&eSSL", Arrays.asList(
                        "&7Encrypt the data when sent",
                        "&7to the server. (Advised)",
                        " ",
                        enabled(manager.getConnectionSettings().SSL)
                ));

        hostname = ItemCreator.createItem(Material.PAPER, "&eHost", "&7" + manager.getConnectionSettings().hostname);
        port = ItemCreator.createItem(Material.PAPER, "&ePort", "&7" + manager.getConnectionSettings().port);

        inventory.setItem(11, autoreconnect);
        inventory.setItem(12, ssl);
        inventory.setItem(13, hostname);
        inventory.setItem(14, port);

    }

    @Override
    public Inventory createInventory(Inventory inventory, Player player, Object... objects) {
        fillInventory(inventory, Items.getGlassPane());
        save = ItemCreator.createItem(Material.BOOK, "&aSave");
        inventory.setItem(15, save);
        return inventory;
    }

    private String enabled(boolean enabled) {
        return ((enabled) ? "&aenabled" : "&cdisabled");
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == e.getWhoClicked().getInventory()) {
            return;
        }


        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if(!p.hasPermission(Permissions.SETUP.getPermission())) {
            p.closeInventory();;
            return;
        }

        ISetupManager manager = MineMonitorApi.getInstance().getSetupManager();
        if(e.getCurrentItem().equals(autoreconnect)) {
            boolean enabled = manager.getConnectionSettings().autoReconnect;
            manager.getConnectionSettings().autoReconnect = !enabled;
            updateInventory(e.getClickedInventory());
            return;
        }

        if(e.getCurrentItem().equals(ssl)) {
            boolean enabled = manager.getConnectionSettings().SSL;
            manager.getConnectionSettings().SSL = !enabled;
            updateInventory(e.getClickedInventory());
            return;
        }

        if(e.getCurrentItem().equals(hostname)) {
            p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_EDIT_SETTING)
                    .replace("%setting", ConnectionSetting.HOSTNAME.toString())
            );
            manager.setEditing(p.getUniqueId(), ConnectionSetting.HOSTNAME);
            p.closeInventory();
            return;
        }

        if(e.getCurrentItem().equals(port)) {
            p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_EDIT_SETTING)
                    .replace("%setting", ConnectionSetting.PORT.toString())
            );
            manager.setEditing(p.getUniqueId(), ConnectionSetting.PORT);
            p.closeInventory();
            return;
        }

        if(e.getCurrentItem().equals(save)) {
            p.closeInventory();
            Guis.SETUP_GUI.openInventory(p);
        }
    }

}
