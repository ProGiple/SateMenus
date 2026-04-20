package org.satellite.dev.progiple.satemenus.menus.params;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.satellite.dev.progiple.satemenus.menus.items.configured.MenuConfiguredItem;
import org.satellite.dev.progiple.satemenus.menus.params.actions.MenuConfiguredAction;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;

import java.util.List;

public interface ITemplated {
    String id();
    IConfig config();
    Template template();
    String title();
    InventoryType type();
    Integer rows();
    Integer updatingTime();
    Integer clickCooldownTicks();
    MenuConfiguredAction openAction();
    MenuConfiguredAction closeAction();
    List<String> getAnimationIds();
    List<MenuConfiguredItem> items();
    ConfigurationSection decorations();
}
