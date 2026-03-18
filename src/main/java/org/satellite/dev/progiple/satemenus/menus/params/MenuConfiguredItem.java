package org.satellite.dev.progiple.satemenus.menus.params;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.utils.SateCache;

import java.util.List;

public record MenuConfiguredItem(@NotNull String id,
                                 @NotNull ConfigurationSection section,
                                 @NotNull List<Integer> slots,
                                 @Nullable ConfigurationSection viewConditions,
                                 @Nullable MenuConfiguredAction anyClick,
                                 @Nullable MenuConfiguredAction rightClick,
                                 @Nullable MenuConfiguredAction leftClick,
                                 @Nullable SateCache cache,
                                 boolean removal) {
    public static MenuConfiguredItem convert(ConfigurationSection section) {
        List<Integer> slots = Utils.getSlotList(section.getStringList("slots"));
        if (slots.isEmpty()) slots.add(section.getInt("slot", -1));

        int cooldownTicks = section.getInt("cooldownTicks");
        SateCache cache = cooldownTicks == 0 ? null : new SateCache(cooldownTicks);

        return new MenuConfiguredItem(
                section.getName(),
                section,
                slots,
                section.getConfigurationSection("view_conditions"),
                MenuConfiguredAction.convert(section.getConfigurationSection("any_click")),
                MenuConfiguredAction.convert(section.getConfigurationSection("right_click")),
                MenuConfiguredAction.convert(section.getConfigurationSection("left_click")),
                cache,
                section.getBoolean("removal")
        );
    }
}
