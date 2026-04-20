package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Getter
public abstract class OnlySelectMenu<E> extends AbstractEditMenu {
    protected final List<Integer> order;
    protected final Collection<E> collection;
    protected final ConfigurationSection selectItemSection;
    public OnlySelectMenu(@NotNull Player player,
                          MenuSettingsBuilder builder,
                          Collection<E> elements) {
        super(player, builder);
        this.collection = elements;

        this.order = Utils.getSlotList(this.config.getStringList("order"));
        this.order.sort(Comparator.comparing(i -> i));

        this.selectItemSection = config.getSection("items.SELECT_ITEM");
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        return null;
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        Iterator<E> iterator = collection.iterator();
        for (int slot : order) {
            if (iterator.hasNext()) {
                byte byteSlot = (byte) slot;
                addItems(false, createSelectItem(iterator.next(), byteSlot).setSlot(byteSlot));
            }
            else
                break;
        }

        super.onOpen(e);
    }

    protected abstract SelectItem<E> createSelectItem(E value, byte slot);
}
