package org.satellite.dev.progiple.satemenus.menus.menus;

import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.menus.IMenu;

@FunctionalInterface
public interface Recreatable {
    IMenu reCreate(Player player);
}
