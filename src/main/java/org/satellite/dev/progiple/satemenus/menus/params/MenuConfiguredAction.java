package org.satellite.dev.progiple.satemenus.menus.params;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.conditions.ExpCondition;
import org.satellite.dev.progiple.satemenus.menus.Refreshable;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.menus.menus.SateMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.menus.params.animations.IAnimation;

import javax.annotation.Nullable;
import java.util.List;

public record MenuConfiguredAction(@Nullable ConfigurationSection conditions,
                                   @NotNull List<String> actions,
                                   boolean cancelEventIfError) {
    public void process(Player target, @Nullable Refreshable refreshable) {
        for (String action : actions) {
            if (action.startsWith("[REFRESH]")) {
                if (refreshable != null) refreshable.refresh();
                continue;
            }

            if (action.startsWith("[BACK]")) {
                IMenu menu = MenuManager.getActiveMenu(target);
                if (menu instanceof Recreatable recreatable) {
                    menu = recreatable.reCreate(target);
                    if (menu != null) MenuManager.openInventory(menu);
                }
                continue;
            }

            if (action.startsWith("[BACK_OR_CLOSE]")) {
                IMenu menu = MenuManager.getActiveMenu(target);
                if (menu instanceof Recreatable recreatable) {
                    menu = recreatable.reCreate(target);
                    if (menu != null) {
                        MenuManager.openInventory(menu);
                        continue;
                    }
                }

                target.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                continue;
            }

            if (action.startsWith("[ANIMATION] ")) {
                IMenu menu = MenuManager.getActiveMenu(target);
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
                section.getBoolean("cancelEvent", false));
    }
}
