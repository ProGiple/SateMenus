package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.ScrollingButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;

import java.util.ArrayList;
import java.util.List;

public class SelectConditionTypeItem extends ScrollingButton<String> {
    private final ManageConditionsMenu.ConditionStruct structure;
    public SelectConditionTypeItem(ConfigurationSection section,
                                   MenuSettingsBuilder builder,
                                   ManageConditionsMenu.ConditionStruct struct) {
        super(section, builder, struct.type);
        this.structure = struct;
    }

    @Override
    public List<String> registerElements() {
        return new ArrayList<>(Conditions.keys());
    }

    @Override
    public void change(HumanEntity player, String newElement) {
        this.structure.type = newElement;
        this.structure.classType = Conditions.getCondition(newElement);
    }
}
