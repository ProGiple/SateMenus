package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.actions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.SwitchItem;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderAction;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.actions.items.ChangeInvokeStatusItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl.EditListMenu;

@MenuId("editMenuAction")
public class EditMenuActionMenu extends AbstractEditMenu implements Recreatable {
    private final MenuBuilderAction builderAction;
    private final Recreatable recreatable;
    public EditMenuActionMenu(@NotNull Player player,
                              MenuSettingsBuilder builder,
                              MenuBuilderAction action,
                              @NotNull Recreatable recreatable) {
        super(player, builder);
        this.builderAction = action;
        this.recreatable = recreatable;
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        return switch (section.getName()) {
            case "EDIT_ACTIONS_BUTTON" -> new SwitchItem(section, false,
                    p -> new EditListMenu(p, builder, builderAction.actions(), this));
            case "EDIT_CONDITIONS_BUTTON" -> new SwitchItem(section, false,
                    p -> new ManageConditionsMenu(p, builder, builderAction.conditionOperation(), builderAction.conditions(), this));
            case "CHANGE_INVOKE_STATUS_BUTTON" -> new ChangeInvokeStatusItem(section, builder, builderAction);
            default -> null;
        };
    }

    @Override
    public boolean back(Player player) {
        if (recreatable != null) recreatable.reCreate(player).open();
        return true;
    }

    @Override
    public IMenu reCreate(Player player) {
        return new EditMenuActionMenu(player, builder, builderAction, recreatable);
    }
}
