package org.satellite.dev.progiple.satemenus.self.menusCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.satellite.dev.progiple.satemenus.menus.params.actions.IMenuAction;
import org.satellite.dev.progiple.satemenus.menus.params.actions.MenuConfiguredAction;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.rejections.OperationRejection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter @Accessors(fluent = true) @Setter
public class MenuBuilderAction implements IMenuAction, Cloneable {
    private final @NotNull List<String> actions = new ArrayList<>();
    private final OperationRejection conditionOperation;
    private boolean cancelEventIfError;
    private final List<ManageConditionsMenu.ConditionStruct> conditions;
    public MenuBuilderAction(MenuConfiguredAction action) {
        conditions = action.conditions() == null ? new ArrayList<>() : action.conditions().getKeys(false)
                .stream()
                .map(k -> {
                    ConfigurationSection conditionSection = action.conditions().getConfigurationSection(k);
                    return MenuConfiguredAction.convertToStruct(conditionSection);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.cancelEventIfError = action.cancelEventIfError();
        this.actions.addAll(action.actions());
        this.conditionOperation = new OperationRejection(MenuConfiguredAction.getOperation(action.conditions()));
    }

    public MenuBuilderAction() {
        this.conditions = new ArrayList<>();
        this.cancelEventIfError = true;
        this.conditionOperation = new OperationRejection(Conditions.Operation.AND);
    }

    @Override
    public MenuBuilderAction clone() {
        MenuBuilderAction cloned = new MenuBuilderAction();
        cloned.actions.addAll(actions);
        cloned.conditionOperation.value = conditionOperation.value;
        cloned.cancelEventIfError = cancelEventIfError;
        cloned.conditions.addAll(conditions.stream().map(ManageConditionsMenu.ConditionStruct::clone).toList());

        return cloned;
    }
}
