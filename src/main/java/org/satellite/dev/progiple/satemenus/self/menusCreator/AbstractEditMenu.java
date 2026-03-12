package org.satellite.dev.progiple.satemenus.self.menusCreator;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;

public abstract class AbstractEditMenu extends AMenu {
    protected final MenuSettingsBuilder builder;
    protected final Configuration config;
    public AbstractEditMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player);
        this.builder = builder;

        String menuPath = this.getClass().getSimpleName().replace(".class", "");
        this.config = new Configuration(SateMenus.getInstance().getDataFolder(), "toolsMenus/" + menuPath);

        String menuName = builder.title() == null ? builder.id() : builder.title();
        this.initialize(
                config.getString("title").replace("[name]", menuName),
                (byte) (config.getInt("rows") * 9),
                config.getSection("decorations"),
                true);
    }
}
