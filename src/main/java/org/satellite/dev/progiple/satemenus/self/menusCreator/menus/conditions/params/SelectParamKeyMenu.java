package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.params;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.OnlySelectMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;

import java.util.Arrays;

@MenuId("selectConditionParamKey")
public class SelectParamKeyMenu extends OnlySelectMenu<String> {
    private final EditConditionParamsMenu.ConditionParamRecord record;
    private final ManageConditionsMenu.ConditionStruct structure;
    private final Recreatable recreatable;
    public SelectParamKeyMenu(@NotNull Player player,
                              MenuSettingsBuilder builder,
                              ManageConditionsMenu.ConditionStruct struct,
                              EditConditionParamsMenu.ConditionParamRecord record,
                              @Nullable Recreatable recreatable) {
        super(player, builder, Arrays.asList(struct.classType.getParams()));
        this.structure = struct;
        this.record = record;
        this.recreatable = recreatable;
    }

    @Override
    protected SelectItem<String> createSelectItem(String value, byte slot) {
        return new SelectKeyItem(selectItemSection, builder, value);
    }

    @Override
    public boolean back(Player player) {
        if (recreatable != null) recreatable.reCreate(player).open();
        return true;
    }

    private class SelectKeyItem extends SelectItem<String> {
        private final String key;
        public SelectKeyItem(ConfigurationSection section,
                             MenuSettingsBuilder builder,
                             String key) {
            super(section, builder);
            this.key = key;
            replaceLore(l -> Utils.applyReplacements(l, "key-%-" + key));
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            Player player = (Player) e.getWhoClicked();
            Object value = structure.values.get(record.key);

            structure.values.remove(record.key);
            structure.values.put(key, value);

            record.key = key;
            back(player);
        }
    }
}
