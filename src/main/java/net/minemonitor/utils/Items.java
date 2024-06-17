package net.minemonitor.utils;

import mcapi.davidout.utils.ItemCreator;
import mcapi.davidout.utils.ServerUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Items {

    public static ItemStack getEnabledItem(boolean enabled) {
        if(enabled) {
            return ItemCreator.getTypeItem("LIME_WOOL", "WOOL", 5);
        }

        return ItemCreator.getTypeItem("RED_WOOL", "WOOL", 14);
    }

    public static ItemStack getGlassPane() {
        return ItemCreator.editItem(ItemCreator.getTypeItem("GRAY_STAINED_GLASS_PANE", "STAINED_GLASS_PANE", 7), " ");
    }

}
