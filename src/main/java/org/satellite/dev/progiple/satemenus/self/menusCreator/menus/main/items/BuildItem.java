package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.EditableButton;

public class BuildItem extends EditableButton {
    public BuildItem(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section, builder);
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        var result = builder.buildAsync();

        e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        result.thenAccept(r -> {
            String partId = builder.generatorType().path.substring(
                    0,
                    builder.generatorType().path.length() - 2);
            partId = Character.toUpperCase(partId.charAt(0)) + partId.substring(1);
            Config.sendMessage(e.getWhoClicked(), "successful" + partId + "Build", "id-%-" + r.id());
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        return this;
    }
}
