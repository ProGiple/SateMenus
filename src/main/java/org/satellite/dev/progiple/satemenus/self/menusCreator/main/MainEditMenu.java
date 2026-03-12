package org.satellite.dev.progiple.satemenus.self.menusCreator.main;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.AbstractEditMenu;

public class MainEditMenu extends AbstractEditMenu {
    public MainEditMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player, builder);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        super.onClose(e);
    }
}
