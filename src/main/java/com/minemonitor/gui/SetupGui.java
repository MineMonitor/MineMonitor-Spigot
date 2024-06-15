package com.minemonitor.gui;

import com.minemonitor.Main;
import com.minemonitor.config.connection.ConfigSetup;
import com.minemonitor.connection.request.Callback;
import com.minemonitor.connection.transfer.response.ServerRegisterDTO;
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

public class SetupGui extends IGui {

    private ItemStack acceptTerms;
    private ItemStack acceptPrivacy;
    private ItemStack connectionSettings;

    private ItemStack save;

    @Override
    public String getTitle(Player player, Object... objects) {
        return "&cRun Setup";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        acceptTerms = (ConfigSetup.getInstance().acceptedTerms()) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&eTerms of Service", Arrays.asList(
                        "&7By continuing to use our services, you acknowledge that",
                        "&7you have read and agree to our Terms of Service.",
                        "&b&n" + Main.getInstance().getDescription().getWebsite() + "/terms-of-service",
                        " ",
                        "&aAccepted"
                )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&eTerms of Service",
                        Arrays.asList(
                                "&7By continuing to use our services, you acknowledge that",
                                "&7you have read and agree to our Terms of Service.",
                                "&b&n" + Main.getInstance().getDescription().getWebsite() + "/terms-of-service",
                                " ",
                                "&cNot accepted"
                        ))
        ;

        acceptPrivacy = (ConfigSetup.getInstance().acceptedPrivacy()) ?
                ItemCreator.editItem(
                        Items.getEnabledItem(true)
                        , "&ePrivacy Policy", Arrays.asList(
                        "&7By continuing to use our services, you acknowledge that",
                        "&7you have read and agree to our Privacy Policy.",
                        "&b&n" + Main.getInstance().getDescription().getWebsite() + "/privacy",
                        " ",
                        "&aAccepted"
                )) :
                ItemCreator.editItem(
                        Items.getEnabledItem(false)
                        , "&ePrivacy Policy",
                        Arrays.asList(
                                "&7By continuing to use our services, you acknowledge that",
                                "&7you have read and agree to our Privacy Policy.",
                                "&b&n" + Main.getInstance().getDescription().getWebsite() + "/privacy",
                                " ",
                                "&cNot accepted"
                        ))
        ;

        connectionSettings = ItemCreator.createItem(
                    ItemCreator.getTypeItem("LEGACY_BOOK_AND_QUILL", "BOOK_AND_QUILL")
                , "&eConnection Settings",
                Arrays.asList(
                        "&7Hostname: &e" +  ConfigSetup.getInstance().getConfig().hostname,
                        "&7Port: &e" + ConfigSetup.getInstance().getConfig().port,
                        "&7Auto reconnect: " + enabled(ConfigSetup.getInstance().getConfig().autoReconnect),
                        "&7SSL: " + enabled(ConfigSetup.getInstance().getConfig().SSL)
                )
                );

        save = (ConfigSetup.getInstance().readyToSave()) ?
                ItemCreator.createItem(Material.BOOK, "&aSave") :
                ItemCreator.createItem(Material.BEDROCK, "&cFinish Setup First", "&7Finish the setup before you can save it.");


        inventory.setItem(11, acceptTerms);
        inventory.setItem(12, acceptPrivacy);
        inventory.setItem(13, connectionSettings);

        inventory.setItem(15, save);
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

        if(!p.hasPermission(Permissions.SETUP.getPermission())) {
            p.closeInventory();;
            return;
        }

        if(e.getCurrentItem().equals(save) && ConfigSetup.getInstance().readyToSave()) {
                p.closeInventory();
                ConfigSetup.getInstance().finishSetup(new Callback<ServerRegisterDTO>(() -> {
                    p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.SETUP_FINISHED));
                }, exception -> {
                    exception.printStackTrace();
                    p.sendMessage(MessageManager.getInstance().getMessage(MessageKey.SETUP_FAILED));
                }));
        }

        if(e.getCurrentItem().equals(acceptTerms)) {
            boolean accepted = ConfigSetup.getInstance().acceptedTerms();
            ConfigSetup.getInstance().setAcceptedTerms(!accepted);
        }

        if(e.getCurrentItem().equals(acceptPrivacy)) {
            boolean accepted = ConfigSetup.getInstance().acceptedPrivacy();
            ConfigSetup.getInstance().setAcceptedPrivacy(!accepted);
        }

        if(e.getCurrentItem().equals(connectionSettings)) {
            Guis.SETUP_CONNECTION_GUI.openInventory(p);
            return;
        }

        this.updateInventory(e.getClickedInventory());
    }

}
