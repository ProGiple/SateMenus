package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.AStorageMenu;

public class DecorationsMenu extends AStorageMenu {
    public DecorationsMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player, builder);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        int size = this.getInventory().getSize();
        builder.decorations().forEach((slot, item) -> {
            if (slot < size)
                getInventory().setItem(slot, item);
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        builder.decorations().clear();
        for (int i = 0; i < getInventory().getSize(); i++) {
            ItemStack item = getInventory().getItem(i);
            if (item != null) builder.decorations().put(i, item);
        }

        super.onClose(e);
    }
}
