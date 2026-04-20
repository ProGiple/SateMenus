package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.EditableButton;

public abstract class SelectItem<E> extends EditableButton {
    public SelectItem(ConfigurationSection section,
                      MenuSettingsBuilder builder) {
        super(section, builder);
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        complete(e);
        return this;
    }

    protected abstract void complete(InventoryClickEvent e);
}
