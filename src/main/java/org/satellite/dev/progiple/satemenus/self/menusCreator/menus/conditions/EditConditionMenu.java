package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.SwitchItem;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.items.SelectConditionTypeItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.params.EditConditionParamsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl.EditListMenu;

@MenuId("editCondition")
public class EditConditionMenu extends AbstractEditMenu implements Recreatable {
    private final ManageConditionsMenu.ConditionStruct structure;
    private final Recreatable recreatable;
    public EditConditionMenu(@NotNull Player player,
                             MenuSettingsBuilder builder,
                             ManageConditionsMenu.ConditionStruct struct,
                             @Nullable Recreatable recreatable) {
        super(player, builder);
        this.structure = struct;
        this.recreatable = recreatable;
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        return switch (section.getName()) {
            case "CHANGE_TYPE_ITEM" -> new SelectConditionTypeItem(section, builder, structure);
            case "TO_MANAGE_PARAMS_ITEM" -> new SwitchItem(section, false,
                    p -> new EditConditionParamsMenu(p, builder, structure, this));
            case "TO_EDIT_ERROR_ACTIONS" -> new SwitchItem(section, false,
                    p -> new EditListMenu(p, builder, structure.errorActions, this));
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
        return new EditConditionMenu(player, builder, structure, recreatable);
    }
}
