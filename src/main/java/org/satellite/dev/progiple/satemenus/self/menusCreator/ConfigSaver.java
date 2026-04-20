package org.satellite.dev.progiple.satemenus.self.menusCreator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.self.Serializer;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigSaver {
    public void save(MenuSettingsBuilder builder, Configuration config) {
        for (String key : config.self().getKeys(false)) {
            config.set(key, null);
        }

        if (!builder.commands().isEmpty()) {
            ConfigurationSection commandsSection = config.createSection((String) null, "commands");
            builder.commands().forEach(commandsSection::set);
        }

        config.setString("title", builder.title());
        if (!SateMenus.CHEST_TYPES.contains(builder.type())) config.setString("type", builder.type().name());
        config.setInt("rows", builder.rows());
        if (builder.updatingTime() > 0) config.setInt("updatingTime", builder.updatingTime());
        if (builder.template() != null) config.setString("template", builder.template().id());
        if (builder.cooldownCachedTicks() > 0) config.setInt("clickCooldownTicks", builder.cooldownCachedTicks());
        if (!builder.animations().isEmpty()) config.setStringList("animations", builder.animations());

        createActionSection(() -> config.createSection((String) null, "open"), builder.openAction());
        createActionSection(() -> config.createSection((String) null, "close"), builder.closeAction());

        Serializer serializer = new Serializer();

        var decorationMap = collectDecorations(builder.decorations());
        serializer.serializeItems(
                () -> config.createSection((String) null, "decorations"),
                decorationMap,
                (i) -> i,
                null,
                null);

        var itemsMap = collectItems(builder.items(), MenuBuilderItem::slot);
        serializer.serializeItems(
                () -> config.createSection((String) null, "items"),
                itemsMap,
                (i) -> i.item().getItemStack(),
                MenuBuilderItem::id,
                (i, s) -> this.serializeMenuBuilderItem(s, i)
        );

        config.save();
    }

    protected void serializeMenuBuilderItem(ConfigurationSection section, MenuBuilderItem builderItem) {
        createConditionSection(
                () -> section.createSection("view_conditions"),
                builderItem.viewConditions(),
                builderItem.viewConditionOperation().value);

        createActionSection(() -> section.createSection("any_click"), builderItem.anyClick());
        createActionSection(() -> section.createSection("right_click"), builderItem.rightClick());
        createActionSection(() -> section.createSection("left_click"), builderItem.leftClick());

        if (builderItem.clickCooldownCache() > 0) section.set("clickCooldownTicks", builderItem.clickCooldownCache());
        if (builderItem.removal()) section.set("removal", true);
    }

    protected Map<ItemStack, List<Integer>> collectDecorations(Map<Integer, ItemStack> decorations) {
        Map<ItemStack, List<Integer>> map = new HashMap<>();
        decorations.forEach((slot, item) -> {
            if (item != null) {
                List<Integer> slots = map.getOrDefault(item, new LinkedList<>());

                slots.add(slot);
                map.put(item, slots);
            }
        });

        return map;
    }

    protected void createActionSection(Supplier<ConfigurationSection> sectionSup, MenuBuilderAction action) {
        if (action.conditions().isEmpty()) return;

        ConfigurationSection section = sectionSup.get();
        if (!action.cancelEventIfError()) section.set("cancelEvent", false);

        createConditionSection(() -> section.createSection("conditions"), action.conditions(), action.conditionOperation().value);
        section.set("actions", action.actions());
    }

    protected void createConditionSection(Supplier<ConfigurationSection> sectionSup,
                                        List<ManageConditionsMenu.ConditionStruct> structs,
                                        Conditions.Operation operation) {
        if (structs.isEmpty()) return;

        ConfigurationSection section = sectionSup.get();
        section.set("operation", operation.name());
        for (int i = 0; i < structs.size();) {
            var struct = structs.get(i);
            String path = "condition" + ++i + ".";

            section.set(path + "type", struct.type);

            String[] keysArray = struct.classType.getParams();
            var paramKeys = new HashSet<>(keysArray == null ? List.of() : List.of(keysArray));

            struct.values.forEach((k, v) -> {
                if (paramKeys.isEmpty() || paramKeys.contains(k))
                    section.set(path + k, v);
            });

            if (!struct.errorActions.isEmpty()) section.set(path + "errorActions", struct.errorActions);
        }
    }

    protected <I> Map<I, List<Integer>> collectItems(Collection<I> items, Function<I, Integer> slotGetter) {
        Map<I, List<Integer>> map = new HashMap<>();
        for (I item : items) {
            if (item != null) {
                List<Integer> slots = map.getOrDefault(item, new LinkedList<>());

                slots.add(slotGetter.apply(item));
                map.put(item, slots);
            }
        }

        return map;
    }
}
