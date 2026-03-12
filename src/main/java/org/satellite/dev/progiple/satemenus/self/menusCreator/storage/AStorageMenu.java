package org.satellite.dev.progiple.satemenus.self.menusCreator.storage;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.IGeneratorMenu;

public abstract class AStorageMenu extends AbstractEditMenu implements IGeneratorMenu {
    public AStorageMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player, builder);
    }

    @Override
    public Configuration getConfig() {
        return this.config;
    }
}
