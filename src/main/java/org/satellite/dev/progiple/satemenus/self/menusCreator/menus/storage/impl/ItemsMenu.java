package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.IEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.AStorageMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.EditItemMenu;

import java.util.HashSet;

public class ItemsMenu extends AStorageMenu {
    public static final String EDIT_ITEM_NBT = "SATEMENUS_EDIT_ITEM_NBT_TAG";

    public ItemsMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player, builder);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        int size = this.getInventory().getSize();
        for (MenuBuilderItem item : builder.items()) {
            if (item.isValid() && item.slot() < size)
                addItems(true, new EditButton(item, (byte) item.slot()));
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();

        var existsItems = new HashSet<MenuBuilderItem>();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType().isAir()) continue;

            var builderItem = builder.items()
                    .stream()
                    .filter(it -> it.isTargetItem(item))
                    .findFirst()
                    .orElse(null);
            if (builderItem == null) {
                builderItem = new MenuBuilderItem();

                var nonMenuItem = NonMenuItem.fromItemStack(item);
                builderItem.item(nonMenuItem);

                builderItem.id("ExampleId-" + Utils.getRKey((byte) 6));
                builder.items().add(builderItem);
            }

            builderItem.slot(i);
            existsItems.add(builderItem);
        }

        builder.items().removeIf(i -> !existsItems.contains(i));
        if (!IEditMenu.INVALID_SAVE_REASONS.contains(e.getReason())) {
            super.onClose(e);
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (e.getClick() != ClickType.MIDDLE || itemStack == null) return;

        Item item = this.getItemList()
                .stream()
                .filter(i -> i instanceof EditButton b && b.builderItem.isTargetItem(itemStack))
                .findFirst()
                .orElse(null);
        if (item == null) {
            var itemBuilder = new MenuBuilderItem();
            NBTManager.setUUID(itemStack, EDIT_ITEM_NBT, itemBuilder.uuid());

            var nonMenuItem = NonMenuItem.fromItemStack(itemStack);
            if (nonMenuItem.getHeadValue() != null) NBTManager.base64head(nonMenuItem.getItemStack(), nonMenuItem.getHeadValue());

            itemBuilder.item(nonMenuItem);
            item = new EditButton(itemBuilder, (byte) e.getSlot());

            itemBuilder.id("ExampleId-" + Utils.getRKey((byte) 6));
            itemBuilder.slot(e.getSlot());

            addItems(false, item);
            builder.items().add(itemBuilder);
        }

        item.onClick(e);
    }

    @Getter
    protected class EditButton extends Item {
        private final MenuBuilderItem builderItem;
        public EditButton(MenuBuilderItem builderItem, @Range(from = 0L, to = 53L) byte slot) {
            super(builderItem.item(), slot);
            this.builderItem = builderItem;
            NBTManager.setUUID(this.getItemStack(), EDIT_ITEM_NBT, builderItem.uuid());
        }

        @Override
        public Item onClick(InventoryClickEvent e) {
            new EditItemMenu((Player) e.getWhoClicked(), builder, builderItem).open();
            return this;
        }
    }
}
