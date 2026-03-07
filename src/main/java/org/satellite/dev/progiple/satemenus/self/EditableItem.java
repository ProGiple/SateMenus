package org.satellite.dev.progiple.satemenus.self;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.items.IgnoreMenuNBT;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.satellite.dev.progiple.satemenus.menus.params.MenuConfiguredItem;

@IgnoreMenuNBT
public class EditableItem extends Item {
    private MenuConfiguredItem configuredItem;
    public EditableItem(MenuConfiguredItem configuredItem, int slot) {
        super(configuredItem.section(), slot);
        this.configuredItem = configuredItem;
    }

    public EditableItem(ItemStack itemStack, int slot) {
        super(NonMenuItem.fromItemStack(itemStack), (byte) slot);
    }


}
