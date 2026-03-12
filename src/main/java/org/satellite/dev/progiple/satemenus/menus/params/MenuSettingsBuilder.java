package org.satellite.dev.progiple.satemenus.menus.params;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;
import org.satellite.dev.progiple.satemenus.self.menusCreator.GeneratorType;
import org.satellite.dev.progiple.satemenus.utils.RegistrableMenuCommand;
import org.satellite.dev.progiple.satemenus.utils.SateCache;

import java.util.*;

@Getter @Setter @Accessors(fluent = true)
public class MenuSettingsBuilder {
    private final GeneratorType generatorType;
    private final String id;
    private final Map<String, Boolean> commands;
    private final List<String> animations;
    private final List<MenuConfiguredItem> items;
    private Template template;
    private String title;
    private InventoryType type;
    private int rows;
    private MenuConfiguredAction openAction;
    private MenuConfiguredAction closeAction;
    private ConfigurationSection decorations;
    private int cooldownCachedTicks;
    private int updatingTime;

    public MenuSettingsBuilder(String id, GeneratorType generatorType) {
        this.id = id;
        this.generatorType = generatorType;
        this.commands = new HashMap<>();
        this.animations = new ArrayList<>();
        this.items = new ArrayList<>();

        IConfig config = new IConfig(SateMenus.getInstance().getDataFolder(), generatorType.path + id);

    }

    public ITemplated build() {
        return switch (generatorType) {
            case MENU -> buildMenuSettings();
            case TEMPLATE -> buildTemplate();
            default -> null;
        };
    }

    public MenuSettings buildMenuSettings() {
        return new MenuSettings(
                new IConfig(SateMenus.getInstance().getDataFolder(), "menus/" + id),
                id,
                commands,
                template,
                title,
                type,
                rows,
                openAction,
                closeAction,
                animations.stream().map(Animations::get).filter(Objects::nonNull).toList(),
                decorations,
                items,
                new SateCache(cooldownCachedTicks),
                updatingTime,
                registrableMenuCommandCreate(id, commands));
    }

    public Template buildTemplate() {
        return new Template(
                new IConfig(SateMenus.getInstance().getDataFolder(), "templates/" + id),
                id,
                title,
                type,
                rows,
                updatingTime,
                template,
                cooldownCachedTicks,
                openAction,
                closeAction,
                animations,
                items,
                decorations);
    }

    public static RegistrableMenuCommand registrableMenuCommandCreate(String id, Map<String, Boolean> commands) {
        List<String> commandList = commands.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
        return new RegistrableMenuCommand(id, commandList);
    }
}
