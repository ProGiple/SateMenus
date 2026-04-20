package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.items;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.InputTextButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.EditItemMenu;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EditItemNameButton extends InputTextButton {
    private final MenuBuilderItem builderItem;
    public EditItemNameButton(ConfigurationSection section,
                              MenuSettingsBuilder builder,
                              MenuBuilderItem item) {
        super(section, builder, item.id());
        this.builderItem = item;
    }

    @Override
    public BiConsumer<InputTextQuery, String> registerAccept() {
        return (q, s) -> {
            builderItem.id(s);

            Player player = Bukkit.getPlayer(q.getUuid());
            new EditItemMenu(player, q.getBuilder(), builderItem).open();
        };
    }

    @Override
    public Consumer<InputTextQuery> registerCancel() {
        return q -> {
            Player player = Bukkit.getPlayer(q.getUuid());
            new EditItemMenu(player, q.getBuilder(), builderItem).open();
        };
    }
}
