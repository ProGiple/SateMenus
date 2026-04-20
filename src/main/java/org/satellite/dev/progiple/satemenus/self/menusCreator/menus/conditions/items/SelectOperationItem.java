package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.rejections.OperationRejection;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.ScrollingButton;

import java.util.List;

public class SelectOperationItem extends ScrollingButton<Conditions.Operation> {
    private final OperationRejection rejection;
    public SelectOperationItem(ConfigurationSection section,
                               MenuSettingsBuilder builder,
                               OperationRejection operation) {
        super(section, builder, operation.value);
        this.rejection = operation;
    }

    @Override
    public List<Conditions.Operation> registerElements() {
        return List.of(Conditions.Operation.values());
    }

    @Override
    public void change(HumanEntity player, Conditions.Operation newElement) {
        this.rejection.value = newElement;
    }
}
