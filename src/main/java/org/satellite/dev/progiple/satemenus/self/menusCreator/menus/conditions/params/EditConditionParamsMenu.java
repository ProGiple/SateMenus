package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.params;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.EditConditionMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.SelectAndAppendMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.AdvancedSelectItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

@MenuId("editConditionParams")
public class EditConditionParamsMenu
        extends SelectAndAppendMenu<EditConditionParamsMenu.ConditionParamRecord>
        implements Recreatable {
    private final ManageConditionsMenu.ConditionStruct structure;
    private final Recreatable recreatable;
    public EditConditionParamsMenu(@NotNull Player player,
                                   MenuSettingsBuilder builder,
                                   ManageConditionsMenu.ConditionStruct struct,
                                   @Nullable Recreatable recreatable) {
        super(player, builder, struct.values.entrySet()
                .stream()
                .map(e -> {
                    var param = new ConditionParamRecord();
                    param.key = e.getKey();
                    param.value = e.getValue();
                    return param;
                })
                .toList());

        this.recreatable = recreatable;
        this.structure = struct;
    }

    @Override
    public boolean back(Player player) {
        if (recreatable != null) recreatable.reCreate(player).open();
        return true;
    }

    @Override
    protected SelectItem<ConditionParamRecord> createAppendedItem(InventoryClickEvent event) {
        var rec = new ConditionParamRecord();
        rec.value = null;

        rec.key = "DefaultKey";

        structure.values.putIfAbsent(rec.key, null);
        return new EditParamItem(selectItemSection, builder, rec);
    }

    @Override
    protected SelectItem<ConditionParamRecord> createSelectItem(ConditionParamRecord value, byte slot) {
        return new EditParamItem(selectItemSection, builder, value);
    }

    @Override
    public IMenu reCreate(Player player) {
        return new EditConditionParamsMenu(player, builder, structure, recreatable);
    }

    private class EditParamItem extends AdvancedSelectItem<ConditionParamRecord> {
        public EditParamItem(ConfigurationSection section,
                             MenuSettingsBuilder builder,
                             ConditionParamRecord record) {
            super(section, builder, record);
            this.replaceLore(l -> Utils.applyReplacements(l,
                    "key-%-" + record.key,
                    "value-%-" + record.value));
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            Player player = (Player) e.getWhoClicked();
            if (e.getClick().isLeftClick()) {
                sendEditMessage(selectItemSection, player);
                var query = new InputTextQuery(player, builder)
                        .thenAccept((q, s) -> {
                            Class<?> clazz = structure.classType.getParamClass(s);
                            if (clazz != null) {
                                try {
                                    value.value = clazz.cast(s);
                                } catch (ClassCastException ignored) {
                                    value.value = s;
                                }
                            }
                            else
                                value.value = s;

                            structure.values.put(value.key, value.value);
                            new EditConditionParamsMenu(player, builder, structure, recreatable).open();
                        })
                        .thenCancel(q -> {
                            new EditConditionParamsMenu(player, builder, structure, recreatable).open();
                        });

                query.register(player.getUniqueId());
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
            else {
                var menu = new SelectParamKeyMenu(player, builder, structure, value, EditConditionParamsMenu.this);
                menu.open();
            }
        }
    }

    public static class ConditionParamRecord {
        public String key;
        public Object value;
    }
}
