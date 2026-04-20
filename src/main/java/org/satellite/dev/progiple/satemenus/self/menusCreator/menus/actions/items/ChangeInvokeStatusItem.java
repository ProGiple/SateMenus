package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.actions.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderAction;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.EditableButton;

public class ChangeInvokeStatusItem extends EditableButton {
    private final MenuBuilderAction builderAction;
    private final ConfigurationSection section;
    public ChangeInvokeStatusItem(ConfigurationSection section,
                                  MenuSettingsBuilder builder,
                                  MenuBuilderAction action) {
        super(section, builder);
        this.builderAction = action;
        this.section = section;
        if (!action.cancelEventIfError()) setAll();
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        builderAction.cancelEventIfError(!builderAction.cancelEventIfError());
        setAll();
        insert();
        return super.onClick(e);
    }

    private void setAll() {
        var section = this.builderAction.cancelEventIfError() ?
                this.section :
                this.section.getConfigurationSection("ifAllowed");
        this.setAll(section);
    }
}
