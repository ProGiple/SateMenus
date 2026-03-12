package org.satellite.dev.progiple.satemenus.self.menusCreator.selectMenus;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.selectMenus.items.SelectItem;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class SelectAndAppendMenu<E> extends OnlySelectMenu<E> {
    private final BiConsumer<Player, SelectItem<E>> onCreate;
    public SelectAndAppendMenu(@NotNull Player player,
                               MenuSettingsBuilder builder,
                               Collection<E> elements,
                               BiFunction<E, Integer, SelectItem<E>> createFunction,
                               BiConsumer<Player, SelectItem<E>> onCreateEvent) {
        super(player, builder, elements, createFunction);
        this.onCreate = onCreateEvent;
    }
}
