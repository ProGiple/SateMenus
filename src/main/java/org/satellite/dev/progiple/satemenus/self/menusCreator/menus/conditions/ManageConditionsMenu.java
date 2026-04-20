package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.conditions.abs.Condition;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.rejections.OperationRejection;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.items.SelectOperationItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.SelectAndAppendMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.AdvancedSelectItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;

import java.util.*;

@MenuId("manageConditions")
public class ManageConditionsMenu
        extends SelectAndAppendMenu<ManageConditionsMenu.ConditionStruct>
        implements Recreatable {
    private final OperationRejection operation;
    private final Recreatable recreatable;
    public ManageConditionsMenu(@NotNull Player player,
                                MenuSettingsBuilder builder,
                                OperationRejection operation,
                                Collection<ConditionStruct> conditions,
                                @Nullable Recreatable recreatable) {
        super(player,
                builder,
                conditions);
        this.operation = operation;
        this.recreatable = recreatable;
    }

    @Override
    protected SelectItem<ConditionStruct> createAppendedItem(InventoryClickEvent event) {
        var struct = new ConditionStruct();

        String conditionType = Conditions.keys().stream().findFirst().orElse(null);
        struct.type = conditionType;
        struct.classType = Conditions.getCondition(conditionType);

        return new ConditionItem(selectItemSection, builder, struct);
    }

    @Override
    protected SelectItem<ConditionStruct> createSelectItem(ConditionStruct value, byte slot) {
        return new ConditionItem(selectItemSection, builder, value);
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        if (section.getName().equalsIgnoreCase("SELECT_OPERATION_ITEM")) {
            return new SelectOperationItem(section, builder, operation);
        }

        return super.initializeItem(section);
    }

    @Override
    public boolean back(Player player) {
        if (this.recreatable != null) recreatable.reCreate(player).open();
        return true;
    }

    @Override
    public IMenu reCreate(Player player) {
        return new ManageConditionsMenu(player, builder, operation, collection, recreatable);
    }

    public static class ConditionStruct implements Cloneable {
        public Condition<?> classType;
        public String type;
        public final Map<String, Object> values = new HashMap<>();
        public final List<String> errorActions = new ArrayList<>();

        @Override
        public ConditionStruct clone() {
            ConditionStruct cloned = new ConditionStruct();
            cloned.values.putAll(values);
            cloned.errorActions.addAll(errorActions);

            cloned.classType = classType;
            cloned.type = type;

            return cloned;
        }
    }

    private class ConditionItem extends AdvancedSelectItem<ConditionStruct> {
        public ConditionItem(ConfigurationSection section,
                             MenuSettingsBuilder builder,
                             ConditionStruct structure) {
            super(section, builder, structure);
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            var menu = new EditConditionMenu((Player) e.getWhoClicked(), builder, value, ManageConditionsMenu.this);
            menu.open();
        }
    }
}
