package com.minemonitor.gui;

import com.minemonitor.Main;
import com.minemonitor.config.ConfigManager;
import com.minemonitor.plugin.DataSetting;
import com.minemonitor.plugin.Permissions;
import com.minemonitor.utils.Items;
import mcapi.davidout.manager.gui.IGui;
import mcapi.davidout.utils.ItemCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginSettingsGui extends IGui {
    @Override
    public String getTitle(Player player, Object... objects) {
        return "&cPlugin Settings";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        List<Integer> places = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17));

        Main.getInstance().getConfigManager().getConfig().getDataSettings().forEach((setting, aBoolean) -> {
            int placeNumber = places.get(0); // Get the first place number
            inventory.setItem(placeNumber,
                    ItemCreator.editItem(
                            Items.getEnabledItem(aBoolean),
                            "&e" + setting.getTitle(),
                            "&7" + setting.getDescription(),
                            " ",
                            enabled(aBoolean)
                    )
            );

            places.remove(0);
        });
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
        if(!p.hasPermission(Permissions.EDIT_CONFIG.getPermission()) || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
            return;
        }

        DataSetting setting = DataSetting.fromTitle(e.getCurrentItem().getItemMeta().getDisplayName());
        if(setting == null) {
            return;
        }

        boolean enabled = Main.getInstance().getConfigManager().getConfig().isEnabled(setting);
        Main.getInstance().getConfigManager().getConfig().setSetting(setting, !enabled);

        try {
            Main.getInstance().getConfigManager().saveConfig(ConfigManager.ConfigType.PLUGIN_CONFIG);
            updateInventory(e.getClickedInventory());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
