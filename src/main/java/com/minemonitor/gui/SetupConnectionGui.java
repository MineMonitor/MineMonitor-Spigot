package com.minemonitor.gui;

import com.minemonitor.config.Setting;
import com.minemonitor.config.connection.ConfigSetup;
import com.minemonitor.plugin.Guis;
import com.minemonitor.plugin.Permissions;
import com.minemonitor.message.MessageKey;
import com.minemonitor.utils.Items;
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
        autoreconnect = (ConfigSetup.getInstance().getConfig().autoReconnect) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&eAuto reconnect", Arrays.asList(
                        "&7Automaticly reconnect to the server when",
                        "&7the connection is closed.",
                        " ",
                        enabled(ConfigSetup.getInstance().getConfig().autoReconnect)
                )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&eAuto reconnect", Arrays.asList(
                        "&7Automaticly reconnect to the server when",
                        "&7the connection is closed.",
                        " ",
                        enabled(ConfigSetup.getInstance().getConfig().autoReconnect)
                ));

        ssl = (ConfigSetup.getInstance().getConfig().SSL) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&eSSL", Arrays.asList(
                        "&7Encrypt the data when sent",
                        "&7to the server. (Advised)",
                        " ",
                        enabled(ConfigSetup.getInstance().getConfig().SSL)
                )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&eSSL", Arrays.asList(
                        "&7Encrypt the data when sent",
                        "&7to the server. (Advised)",
                        " ",
                        enabled(ConfigSetup.getInstance().getConfig().SSL)
                ));

        hostname = ItemCreator.createItem(Material.PAPER, "&eHost", "&7" + ConfigSetup.getInstance().getConfig().hostname);
        port = ItemCreator.createItem(Material.PAPER, "&ePort", "&7" + ConfigSetup.getInstance().getConfig().port);

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

        if(e.getCurrentItem().equals(autoreconnect)) {
            boolean enabled = ConfigSetup.getInstance().getConfig().autoReconnect;
            ConfigSetup.getInstance().getConfig().autoReconnect = !enabled;
            updateInventory(e.getClickedInventory());
            return;
        }

        if(e.getCurrentItem().equals(ssl)) {
            boolean enabled = ConfigSetup.getInstance().getConfig().SSL;
            ConfigSetup.getInstance().getConfig().SSL = !enabled;
            updateInventory(e.getClickedInventory());
            return;
        }

        if(e.getCurrentItem().equals(hostname)) {
            p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_EDIT_SETTING)
                    .replace("%setting", Setting.HOSTNAME.toString())
            );
            ConfigSetup.getInstance().setEditing(p.getUniqueId(), Setting.HOSTNAME);
            p.closeInventory();
            return;
        }

        if(e.getCurrentItem().equals(port)) {
            p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.CONNECTION_EDIT_SETTING)
                    .replace("%setting", Setting.PORT.toString())
            );
            ConfigSetup.getInstance().setEditing(p.getUniqueId(), Setting.PORT);
            p.closeInventory();
            return;
        }

        if(e.getCurrentItem().equals(save)) {
            p.closeInventory();
            Guis.SETUP_GUI.openInventory(p);
        }
    }

}
