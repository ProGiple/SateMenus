package org.satellite.dev.progiple.satemenus.self.menusCreator.menus;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;

import java.util.EnumSet;
import java.util.Set;

public interface IEditMenu extends ItemListMenu {
    Set<InventoryCloseEvent.Reason> INVALID_SAVE_REASONS = EnumSet.of(
            InventoryCloseEvent.Reason.OPEN_NEW, InventoryCloseEvent.Reason.PLUGIN
    );

    MenuSettingsBuilder getBuilder();
}
