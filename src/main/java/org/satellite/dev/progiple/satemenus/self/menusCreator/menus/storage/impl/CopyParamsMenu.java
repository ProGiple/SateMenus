package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.EditItemMenu;

public class CopyParamsMenu extends ItemsMenu {
    private final MenuBuilderItem builderItem;
    private final boolean linkEditing;
    public CopyParamsMenu(@NotNull Player player,
                          MenuSettingsBuilder builder,
                          MenuBuilderItem builderItem,
                          boolean linkEditing) {
        super(player, builder);
        this.builderItem = builderItem;
        this.linkEditing = linkEditing;
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(SateMenus.getInstance(), () -> {
            new EditItemMenu(getPlayer(), builder, builderItem).open();
        }, 3L);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        var item = this.findFirstItem(e.getCurrentItem(), e.getSlot());
        if (item instanceof EditButton eb) {
            var parentItem = eb.getBuilderItem();
            if (parentItem.equals(builderItem)) {
                e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                return;
            }

            parentItem.copyTo(builderItem, linkEditing);
            e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }
    }
}
