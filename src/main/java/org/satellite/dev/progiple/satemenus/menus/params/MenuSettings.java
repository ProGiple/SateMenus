package org.satellite.dev.progiple.satemenus.menus.params;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.params.animations.IAnimation;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;
import org.satellite.dev.progiple.satemenus.utils.RegistrableMenuCommand;
import org.satellite.dev.progiple.satemenus.utils.SateCache;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter @Accessors(fluent = true)
public class MenuSettings {
    private final IConfig config;
    private final String id;
    private final Map<String, Boolean> commands;
    private final Template template;
    private final String title;
    private final InventoryType type;
    private final int rows;
    private final MenuConfiguredAction openAction;
    private final MenuConfiguredAction closeAction;
    private final List<IAnimation> animations;
    private final ConfigurationSection decorations;
    private final List<MenuConfiguredItem> items;
    private final SateCache cooldownCache;
    private final int updatingTime;
    private final RegistrableMenuCommand registrableCommand;
    public MenuSettings(IConfig config) {
        this.config = config;
        this.id = config.getFile().getName().replace(".yml", "");
        this.commands = new HashMap<>();
        this.template = Templates.getTemplate(config.getString("template"));
        this.title = ColorManager.color(loadParameter(config.getString("title"), Template::title));
        this.type = loadParameter(Utils.getEnumValue(InventoryType.class, config.getString("type")), Template::type);

        Integer rows = loadParameter(Template.getInt(config.getString("rows")), Template::rows);
        this.rows = rows == null ? 0 : rows;

        Integer updatingTime = loadParameter(Template.getInt(config.getString("updatingTime")), Template::updatingTime);
        this.updatingTime = updatingTime == null ? 0 : updatingTime;

        this.openAction = loadParameter(MenuConfiguredAction.convert(config.getSection("open")), Template::openAction);
        this.closeAction = loadParameter(MenuConfiguredAction.convert(config.getSection("close")), Template::closeAction);
        this.decorations = loadParameter(config.getSection("decorations"), Template::decorations);

        Integer clickCooldownTicks = loadParameter(Template.getInt(config.getString("clickCooldownTicks")), Template::clickCooldownTicks);
        if (clickCooldownTicks == null || clickCooldownTicks == 0)
            this.cooldownCache = null;
        else
            this.cooldownCache = new SateCache(clickCooldownTicks);

        List<String> animations = config.getStringList("animations");
        if (animations.isEmpty() && template != null) animations = template.animations();
        this.animations = animations.isEmpty() ? new ArrayList<>() : animations
                .stream()
                .map(Animations::get)
                .filter(Objects::nonNull)
                .toList();

        this.items = Template.loadItems(config, this.template);

        ConfigurationSection commandsSection = config.getSection("commands");
        if (commandsSection != null) {
            for (String key : commandsSection.getKeys(false)) {
                boolean value = commandsSection.getBoolean(key);
                this.commands.put(key, value);
            }
        }

        List<String> commands = this.commands.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
        this.registrableCommand = new RegistrableMenuCommand(this, commands);
    }

    protected @Nullable <E> E loadParameter(E obj, Template template, Function<Template, E> ifNull) {
        if (obj == null && template != null) {
            obj = loadParameter(ifNull.apply(template), template.template(), ifNull);
        }
        return obj;
    }

    protected <E> E loadParameter(E obj, Function<Template, E> ifNull) {
        return loadParameter(obj, this.template, ifNull);
    }

    public boolean checkConditions(Player player, ConfigurationSection section) {
        if (section == null) return true;
        return Conditions.checkConditions(player, section);
    }

    public void unload() {
        this.registrableCommand.unregister();
    }

    public void load() {
        this.registrableCommand.register();
    }
}
