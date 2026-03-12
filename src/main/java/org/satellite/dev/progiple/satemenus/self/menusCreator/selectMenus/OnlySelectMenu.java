package org.satellite.dev.progiple.satemenus.self.menusCreator.selectMenus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.selectMenus.items.SelectItem;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public class OnlySelectMenu<E> extends AbstractEditMenu {
    protected final List<Integer> order;
    protected final Collection<E> collection;
    private final BiFunction<E, Integer, SelectItem<E>> createFunction;
    public OnlySelectMenu(@NotNull Player player,
                          MenuSettingsBuilder builder,
                          Collection<E> elements,
                          BiFunction<E, Integer, SelectItem<E>> createFunction) {
        super(player, builder);
        this.collection = elements;
        this.createFunction = createFunction;

        this.order = Utils.getSlotList(this.config.getStringList("order"));
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }
}
