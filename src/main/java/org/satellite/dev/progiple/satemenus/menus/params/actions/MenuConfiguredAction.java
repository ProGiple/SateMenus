package org.satellite.dev.progiple.satemenus.menus.params.actions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.Refreshable;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.menus.Backable;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.menus.params.animations.IAnimation;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;

import javax.annotation.Nullable;
import java.util.List;

public record MenuConfiguredAction(@Nullable ConfigurationSection conditions,
                                   @NotNull List<String> actions,
                                   boolean cancelEventIfError) implements IMenuAction {
    public void process(Player target, @Nullable Refreshable refreshable) {
        IMenu menu = MenuManager.getActiveMenu(target);
        for (String action : actions) {
            if (action.startsWith("[REFRESH]")) {
                if (refreshable != null) refreshable.refresh();
                continue;
            }

            if (action.startsWith("[REFRESH_MENU]")) {
                if (menu instanceof Refreshable rfr)
                    rfr.refresh();
                continue;
            }

            if (action.startsWith("[BACK]")) {
                if (menu instanceof Backable backable) {
                    backable.back(target);
                }
                continue;
            }

            if (action.startsWith("[BACK_OR_CLOSE]")) {
                if (menu instanceof Backable backable) {
                    if (backable.back(target)) continue;
                }

                target.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                continue;
            }

            if (action.startsWith("[ANIMATION] ")) {
                if (menu instanceof AnimatedMenu animatedMenu) {
                    String id = action.replace("[ANIMATION] ", "");

                    IAnimation animation = Animations.get(id);
                    if (animation != null) animation.play(animatedMenu);
                }
                continue;
            }

            Utils.processCommandsWithActions(target, action, "player-%-" + target.getName());
        }
    }

    public static MenuConfiguredAction convert(ConfigurationSection section) {
        if (section == null) return null;
        return new MenuConfiguredAction(
                section.getConfigurationSection("conditions"),
                section.getStringList("actions"),
                section.getBoolean("cancelEvent", true));
    }

    public static ManageConditionsMenu.ConditionStruct convertToStruct(ConfigurationSection conditionSection) {
        if (conditionSection == null) return null;
        String type = conditionSection.getString("type");

        var condition = Conditions.getCondition(type);
        if (condition == null) return null;

        var struct = new ManageConditionsMenu.ConditionStruct();
        struct.type = type;
        struct.classType = condition;
        struct.errorActions.addAll(conditionSection.getStringList("errorActions"));

        for (String param : condition.getParams()) {
            if (param == null) continue;
            struct.values.put(param, conditionSection.get(param));
        }

        return struct;
    }

    public static Conditions.Operation getOperation(ConfigurationSection section) {
        return section == null ?
                Conditions.Operation.AND :
                Utils.getEnumValue(
                        Conditions.Operation.class,
                        section.getString("operation"),
                        Conditions.Operation.AND);
    }
}
