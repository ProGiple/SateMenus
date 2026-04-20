package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.EditableButton;

public class ChangeRemovalStatusButton extends EditableButton {
    private final MenuBuilderItem builderItem;
    private final ConfigurationSection section;
    public ChangeRemovalStatusButton(ConfigurationSection section,
                                     MenuSettingsBuilder builder,
                                     MenuBuilderItem builderItem) {
        super(section, builder);
        this.builderItem = builderItem;
        this.section = section;
        if (builderItem.removal()) setAll(section.getConfigurationSection("ifRemoval"));
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        builderItem.removal(!builderItem.removal());
        return this.setAll(getSection(section, builderItem.removal())).insert();
    }

    private static ConfigurationSection getSection(ConfigurationSection base, boolean state) {
        return state ? base.getConfigurationSection("ifRemoval") : base;
    }
}
