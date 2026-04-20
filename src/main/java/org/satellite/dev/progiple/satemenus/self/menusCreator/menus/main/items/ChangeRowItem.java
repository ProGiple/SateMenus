package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.ScrollingButton;

import java.util.List;

public class ChangeRowItem extends ScrollingButton<Integer> {
    public ChangeRowItem(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section, builder, builder.rows());
    }

    @Override
    public List<Integer> registerElements() {
        return List.of(1, 2, 3, 4, 5, 6);
    }

    @Override
    public void change(HumanEntity player, Integer newElement) {
        builder.rows(newElement);
    }
}
