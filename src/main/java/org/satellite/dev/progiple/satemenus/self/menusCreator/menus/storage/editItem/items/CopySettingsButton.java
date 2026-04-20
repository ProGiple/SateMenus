package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.EditableButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl.CopyParamsMenu;

public class CopySettingsButton extends EditableButton {
    private final MenuBuilderItem builderItem;
    public CopySettingsButton(ConfigurationSection section,
                              MenuSettingsBuilder builder,
                              MenuBuilderItem builderItem) {
        super(section, builder);
        this.builderItem = builderItem;
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        boolean linkEditing = e.getClick().isShiftClick();
        new CopyParamsMenu((Player) e.getWhoClicked(), builder, builderItem, linkEditing).open();
        return this;
    }
}
