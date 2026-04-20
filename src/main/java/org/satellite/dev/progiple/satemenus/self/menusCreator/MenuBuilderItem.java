package org.satellite.dev.progiple.satemenus.self.menusCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.satellite.dev.progiple.satemenus.menus.items.configured.IMenuItem;
import org.satellite.dev.progiple.satemenus.menus.items.configured.MenuConfiguredItem;
import org.satellite.dev.progiple.satemenus.menus.params.actions.MenuConfiguredAction;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl.ItemsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.rejections.OperationRejection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Accessors(fluent = true)
public class MenuBuilderItem implements IMenuItem {
    private final UUID uuid = UUID.randomUUID();
    private List<ManageConditionsMenu.ConditionStruct> viewConditions = new ArrayList<>();
    private OperationRejection viewConditionOperation;
    private String id;
    private MenuBuilderAction anyClick;
    private MenuBuilderAction rightClick;
    private MenuBuilderAction leftClick;
    private long clickCooldownCache = 0;
    private boolean removal = false;
    private int slot = -1;
    private NonMenuItem item;

    public MenuBuilderItem(MenuConfiguredItem configuredItem, int slot) {
        if (configuredItem.viewConditions() != null)
            for (String key : configuredItem.viewConditions().getKeys(false)) {
                ConfigurationSection section = configuredItem.viewConditions().getConfigurationSection(key);

                var struct = MenuConfiguredAction.convertToStruct(section);
                if (struct != null) viewConditions.add(struct);
            }

        this.viewConditionOperation = new OperationRejection(MenuConfiguredAction.getOperation(configuredItem.viewConditions()));
        this.id = configuredItem.section().getName();
        this.anyClick = initializeAction(configuredItem.anyClick());
        this.rightClick = initializeAction(configuredItem.rightClick());
        this.leftClick = initializeAction(configuredItem.leftClick());
        this.clickCooldownCache = configuredItem.clickCooldownCache();
        this.removal = configuredItem.removal();
        this.slot = slot;
        this.item = new NonMenuItem(configuredItem.section());
    }

    public MenuBuilderItem() {
        this.viewConditionOperation = new OperationRejection(Conditions.Operation.AND);
        this.anyClick = new MenuBuilderAction();
        this.rightClick = new MenuBuilderAction();
        this.leftClick = new MenuBuilderAction();
    }

    public boolean isValid() {
        return id != null && slot >= 0 && item != null;
    }

    public boolean isTargetItem(ItemStack item) {
        if (!isValid() || item == null) return false;

        UUID targetUUID = NBTManager.getUUID(item, ItemsMenu.EDIT_ITEM_NBT);
        return targetUUID != null && targetUUID.equals(uuid);
    }

    private MenuBuilderAction initializeAction(MenuConfiguredAction action) {
        return action == null ? new MenuBuilderAction() : new MenuBuilderAction(action);
    }

    public void copyTo(MenuBuilderItem target, boolean linking) {
        if (linking) {
            target.viewConditions(this.viewConditions());
            target.viewConditionOperation(this.viewConditionOperation());
            target.id(this.id());
            target.anyClick(this.anyClick());
            target.rightClick(this.rightClick());
            target.leftClick(this.leftClick());
            target.clickCooldownCache(this.clickCooldownCache());
            target.removal(this.removal());
            target.item(this.item());
        }
        else {
            target.viewConditions().clear();
            target.viewConditions().addAll(this.viewConditions()
                    .stream()
                    .map(ManageConditionsMenu.ConditionStruct::clone)
                    .toList());

            target.viewConditionOperation().value = this.viewConditionOperation().value;
            target.id(this.id());
            target.anyClick(this.anyClick().clone());
            target.rightClick(this.rightClick().clone());
            target.leftClick(this.leftClick().clone());
            target.clickCooldownCache(this.clickCooldownCache());
            target.removal(this.removal());
        }
    }
}
