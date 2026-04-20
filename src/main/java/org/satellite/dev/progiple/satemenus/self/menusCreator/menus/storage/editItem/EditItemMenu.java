package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.SwitchItem;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuBuilderItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.InputTextButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.actions.EditMenuActionMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.items.ChangeRemovalStatusButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.items.CopySettingsButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.editItem.items.EditItemNameButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl.ItemsMenu;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@MenuId("editItem")
public class EditItemMenu extends AbstractEditMenu implements Recreatable {
    private final MenuBuilderItem builderItem;
    public EditItemMenu(@NotNull Player player,
                        MenuSettingsBuilder builder,
                        MenuBuilderItem item) {
        super(player, builder);
        this.builderItem = item;
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        return switch (section.getName()) {
            case "EDIT_ITEM_NAME_BUTTON" -> new EditItemNameButton(section, builder, builderItem);
            case "MANAGE_CONDITIONS_BUTTON" -> new SwitchItem(section, false,
                    p -> new ManageConditionsMenu(p, builder, builderItem.viewConditionOperation(), builderItem.viewConditions(), this));
            case "EDIT_ANY_CLICK_BUTTON" -> new SwitchItem(section, false,
                    p -> new EditMenuActionMenu(p, builder, builderItem.anyClick(), this));
            case "EDIT_LEFT_CLICK_BUTTON" -> new SwitchItem(section, false,
                    p -> new EditMenuActionMenu(p, builder, builderItem.leftClick(), this));
            case "EDIT_RIGHT_CLICK_BUTTON" -> new SwitchItem(section, false,
                    p -> new EditMenuActionMenu(p, builder, builderItem.rightClick(), this));
            case "EDIT_CLICK_COOLDOWN_VALUE" -> new WriteNumberItem(section, builder, builderItem.clickCooldownCache(), builderItem::clickCooldownCache);
            case "EDIT_SLOT_VALUE" -> new WriteNumberItem(section, builder, builderItem.slot(), l -> builderItem.slot(l.intValue()));
            case "CHANGE_REMOVAL_STATE_BUTTON" -> new ChangeRemovalStatusButton(section, builder, builderItem);
            case "COPY_PARAMS_BUTTON" -> new CopySettingsButton(section, builder, builderItem);
            default -> null;
        };
    }

    @Override
    public boolean back(Player player) {
        new ItemsMenu(player, builder).open();
        return true;
    }

    @Override
    public IMenu reCreate(Player player) {
        return new EditItemMenu(player, builder, builderItem);
    }

    private class WriteNumberItem extends InputTextButton {
        private final Consumer<Long> consumer;
        public WriteNumberItem(ConfigurationSection section,
                               MenuSettingsBuilder builder,
                               long nowValue,
                               Consumer<Long> consumer) {
            super(section, builder, String.valueOf(nowValue));
            this.consumer = consumer;
        }

        @Override
        public BiConsumer<InputTextQuery, String> registerAccept() {
            return (q, s) -> {
                long value = LunaMath.toLong(s, -3);
                if (value != -3) consumer.accept(value);

                new EditItemMenu(getPlayer(), builder, builderItem).open();
            };
        }

        @Override
        public Consumer<InputTextQuery> registerCancel() {
            return q -> new EditItemMenu(getPlayer(), builder, builderItem).open();
        }
    }
}
