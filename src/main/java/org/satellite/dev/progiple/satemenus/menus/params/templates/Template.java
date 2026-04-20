package org.satellite.dev.progiple.satemenus.menus.params.templates;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.params.ITemplated;
import org.satellite.dev.progiple.satemenus.menus.params.actions.MenuConfiguredAction;
import org.satellite.dev.progiple.satemenus.menus.items.configured.MenuConfiguredItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Template(@NotNull IConfig config,
                       @NotNull String id,
                       @Nullable String title,
                       @Nullable InventoryType type,
                       @Nullable Integer rows,
                       @Nullable Integer updatingTime,
                       @Nullable Template template,
                       @Nullable Integer clickCooldownTicks,
                       @Nullable MenuConfiguredAction openAction,
                       @Nullable MenuConfiguredAction closeAction,
                       @NotNull List<String> animations,
                       @NotNull List<MenuConfiguredItem> items,
                       @Nullable ConfigurationSection decorations) implements ITemplated {
    @Override
    public List<String> getAnimationIds() {
        return animations;
    }

    public static Template convert(IConfig config) {
        Template baseTemplate = Templates.getTemplate(config.getString("template"));
        return new Template(
                config,
                config.getFile().getName().replace(".yml", ""),
                config.self().getString("title", null),
                Utils.getEnumValue(InventoryType.class, config.getString("type")),
                getInt(config.getString("rows")),
                getInt(config.getString("updatingTime")),
                baseTemplate,
                getInt(config.getString("clickCooldownTicks")),
                MenuConfiguredAction.convert(config.getSection("open")),
                MenuConfiguredAction.convert(config.getSection("close")),
                config.getStringList("animations"),
                loadItems(config, baseTemplate),
                config.getSection("decorations"));
    }

    public static List<MenuConfiguredItem> loadItems(IConfig config, @Nullable Template template) {
        List<MenuConfiguredItem> list = new ArrayList<>();

        ConfigurationSection section = config.getSection("items");
        if (section == null) return list;

        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection != null) {
                MenuConfiguredItem item = MenuConfiguredItem.convert(itemSection);
                list.add(item);
            }
        }

        if (template != null) {
            Set<String> ids = new HashSet<>();
            list.forEach(item -> ids.add(item.id()));

            List<MenuConfiguredItem> templateItems = template.items()
                    .stream()
                    .filter(i -> !ids.contains(i.id()))
                    .toList();
            list.addAll(templateItems);
        }

        return list;
    }

    public static Integer getInt(String line) {
        return line == null || line.isEmpty() ? null : LunaMath.toInt(line);
    }
}
