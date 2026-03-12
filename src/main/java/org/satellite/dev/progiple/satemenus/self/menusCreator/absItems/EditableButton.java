package org.satellite.dev.progiple.satemenus.self.menusCreator.absItems;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;

@Getter
public abstract class EditableButton extends Item {
    protected final MenuSettingsBuilder builder;
    public EditableButton(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section);
        this.builder = builder;
    }
}
