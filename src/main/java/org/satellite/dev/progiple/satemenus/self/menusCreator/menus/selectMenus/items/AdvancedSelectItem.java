package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.SelectAndAppendMenu;

@Getter @Setter
public abstract class AdvancedSelectItem<E> extends SelectItem<E> {
    protected E value;
    public AdvancedSelectItem(ConfigurationSection section,
                              MenuSettingsBuilder builder,
                              E value) {
        super(section, builder);
        this.value = value;
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getClick() == ClickType.MIDDLE) {
            var menu = (SelectAndAppendMenu<E>) this.getMenu();
            menu.remove(this);
        }
        else
            complete(e);
        return this;
    }

    public void onAppend(SelectAndAppendMenu<E> menu) {
        try {
            menu.getCollection().add(value);
        } catch (UnsupportedOperationException e) {
            Utils.debug(e.getMessage());
        }
    }

    public void onRemove(SelectAndAppendMenu<E> menu) {
        try {
            menu.getCollection().remove(value);
        } catch (UnsupportedOperationException e) {
            Utils.debug(e.getMessage());
        }
    }
}
