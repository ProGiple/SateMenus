package org.satellite.dev.progiple.satemenus.menus;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.menus.menus.impl.SateMenu;
import org.satellite.dev.progiple.satemenus.menus.menus.impl.UpdatableSateMenu;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Menus {
    @Getter
    private final Map<String, MenuSettings> menuSettings = new HashMap<>();

    public SateMenu open(Player player, MenuSettings settings, boolean bypassConditions) {
        bypassConditions = bypassConditions || player.hasPermission("satemenus.openBypass");

        var openActions = settings.openAction();
        if (bypassConditions || openActions == null || settings.checkConditions(player, openActions.conditions())) {
            SateMenu menu = openBypassed(player, settings);
            if (openActions != null) openActions.process(player, menu);
            return menu;
        }

        if (settings.openAction().cancelEventIfError()) {
            return null;
        }

        return openBypassed(player, settings);
    }

    public SateMenu open(Player player, MenuSettings settings) {
        return open(player, settings, false);
    }

    public SateMenu openBypassed(Player player, MenuSettings settings) {
        IMenu openedMenu = MenuManager.getActiveMenu(player);

        Recreatable recreatable;
        if (openedMenu instanceof Recreatable r)
            recreatable = r;
        else if (openedMenu instanceof AMenu a)
            recreatable = a::copy;
        else
            recreatable = null;

        SateMenu menu = settings.updatingTime() <= 0 ?
                new SateMenu(player, settings, recreatable) :
                new UpdatableSateMenu(player, settings, recreatable);
        menu.open();
        return menu;
    }

    public void register(MenuSettings settings) {
        MenuSettings old = menuSettings.get(settings.id());
        if (old != null) unregister(old);

        menuSettings.put(settings.id(), settings);
        settings.load();
    }

    public void unregister(MenuSettings settings) {
        menuSettings.remove(settings.id());
        settings.unload();
    }

    public MenuSettings getSettings(String id) {
        return menuSettings.get(id);
    }

    public void loadFromDir(File directory) {
        if (!directory.exists() || !directory.isDirectory() || directory.listFiles() == null) return;

        File[] files = directory.listFiles();
        for (File file : files) register(new MenuSettings(new IConfig(file)));
    }
}
