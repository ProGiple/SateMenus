package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.SelectAndAppendMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.AdvancedSelectItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.Collection;

@MenuId("editList")
public class EditListMenu extends SelectAndAppendMenu<String> {
    private final Recreatable recreatable;
    public EditListMenu(@NotNull Player player,
                        MenuSettingsBuilder builder,
                        Collection<String> elements,
                        @Nullable Recreatable recreatable) {
        super(player, builder, elements);
        this.recreatable = recreatable;
    }

    @Override
    protected SelectItem<String> createAppendedItem(InventoryClickEvent e) {
        return new EditLineItem(selectItemSection, builder, "ExampleLine");
    }

    @Override
    protected SelectItem<String> createSelectItem(String value, byte slot) {
        return new EditLineItem(selectItemSection, builder, value);
    }

    @Override
    public boolean back(Player player) {
        if (recreatable != null) recreatable.reCreate(player).open();
        return true;
    }

    private class EditLineItem extends AdvancedSelectItem<String> {
        public EditLineItem(ConfigurationSection section,
                            MenuSettingsBuilder builder,
                            String line) {
            super(section, builder, line);
            this.replaceLore(l -> Utils.applyReplacements(l, ColorManager.color("line-%-" + line)));
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            Player player = (Player) e.getWhoClicked();
            InputTextQuery query = new InputTextQuery(player, builder)
                    .thenAccept((q, s) -> {
                        collection.remove(value);
                        collection.add(s);

                        new EditListMenu(player, builder, collection, recreatable).open();
                    })
                    .thenCancel(q -> {
                        new EditListMenu(player, builder, collection, recreatable).open();
                    });

            sendEditMessage(selectItemSection, player);
            query.register(player.getUniqueId());
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
    }
}
