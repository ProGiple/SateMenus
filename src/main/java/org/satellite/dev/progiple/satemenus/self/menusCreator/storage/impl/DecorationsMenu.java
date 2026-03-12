package org.satellite.dev.progiple.satemenus.self.menusCreator.storage.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.storage.AStorageMenu;

public class DecorationsMenu extends AStorageMenu {
    public DecorationsMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player, builder);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }
}
