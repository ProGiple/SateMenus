package org.satellite.dev.progiple.satemenus.self.menusCreator.absItems;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.function.BiConsumer;

public abstract class InputTextButton extends EditableButton {
    private final BiConsumer<InputTextQuery, String> consumer;
    public InputTextButton(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section, builder);
        this.consumer = registerConsumer();
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        player.closeInventory();

        var inputTextQuery = new InputTextQuery(player, builder, consumer);
        inputTextQuery.register(player.getUniqueId());

        return this;
    }

    public abstract BiConsumer<InputTextQuery, String> registerConsumer();
}