package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.EditableButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.AdvancedSelectItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;

import java.util.*;

public abstract class SelectAndAppendMenu<E> extends OnlySelectMenu<E> {
    public SelectAndAppendMenu(@NotNull Player player,
                               MenuSettingsBuilder builder,
                               Collection<E> elements) {
        super(player, builder, elements);
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        if (section.getName().equalsIgnoreCase("APPEND_ITEM")) {
            int slot = getNextSlot();
            return slot == -1 ? null : new AppendItem(section).setSlot((byte) slot);
        }
        return null;
    }

    public void append(SelectItem<E> item) {
        Item appendItem = findFirstItem(AppendItem.class);
        if (appendItem != null) {
            int nextSlot = getNextSlot();

            item.setSlot(appendItem.getSlot());

            appendItem.remove(this);
            if (nextSlot != -1) addItems(true, appendItem.setSlot((byte) nextSlot));

            addItems(true, item);
            if (item instanceof AdvancedSelectItem<E> asi)
                asi.onAppend(this);
        }
    }

    public void remove(SelectItem<E> selectedItem) {
        if (selectedItem instanceof AdvancedSelectItem<E> asi)
            asi.onRemove(this);

        List<Item> items = new ArrayList<>(this.findItems(SelectItem.class));
        items.forEach(i -> i.remove(this));
        items.remove(selectedItem);

        Item appendItem = this.findFirstItem(AppendItem.class);
        if (appendItem != null) {
            appendItem.remove(this);
        }

        Iterator<E> iterator = collection.iterator();
        for (int slot : order) {
            if (iterator.hasNext()) {
                byte byteSlot = (byte) slot;
                addItems(false, createSelectItem(iterator.next(), byteSlot).setSlot(byteSlot));
            }
            else
                break;
        }

        int nextSlot = getNextSlot();
        if (nextSlot != -1) {
            appendItem = new AppendItem(config.getSection("items.APPEND_ITEM"));
            this.addItems(true, appendItem.setSlot((byte) nextSlot));
        }

        insertAll();
    }

    protected int getNextSlot() {
        return order.stream().filter(i -> findFirstItem(i) == null).findFirst().orElse(-1);
    }

    protected abstract SelectItem<E> createAppendedItem(InventoryClickEvent event);

    public class AppendItem extends EditableButton {
        public AppendItem(ConfigurationSection section) {
            super(section, SelectAndAppendMenu.this.builder);
        }

        @Override
        public Item onClick(InventoryClickEvent e) {
            e.setCancelled(true);

            append(createAppendedItem(e));
            return this;
        }
    }
}
