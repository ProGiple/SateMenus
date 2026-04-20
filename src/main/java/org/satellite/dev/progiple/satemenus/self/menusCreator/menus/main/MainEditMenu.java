package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.items.ConsumerItem;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.SwitchItem;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.AbstractEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.GeneratorType;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.InputTextButton;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.actions.EditMenuActionMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.items.BuildItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.items.ChangeInventoryTypeItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.items.ChangeRowItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl.ManageAnimationsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl.ManageCommandsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl.SelectTemplateMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl.DecorationsMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage.impl.ItemsMenu;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@MenuId("mainEdit")
public class MainEditMenu extends AbstractEditMenu implements Recreatable {
    public MainEditMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player, builder);
    }

    @Override
    public Item initializeItem(ConfigurationSection section) {
        return switch (section.getName().toUpperCase()) {
            case "UPDATING_TIME_ITEM" -> new MainInputTextButton(
                    section,
                    s -> builder.updatingTime(Math.min(LunaMath.toInt(s), 0)),
                    String.valueOf(builder.updatingTime()));
            case "COOLDOWN_CLICK_ITEM" -> new MainInputTextButton(
                    section,
                    s -> builder.cooldownCachedTicks(Math.min(LunaMath.toInt(s), 0)),
                    String.valueOf(builder.cooldownCachedTicks()));
            case "CHANGE_ROWS_ITEM" -> new ChangeRowItem(section, builder);
            case "CHANGE_INV_TYPE_ITEM" -> new ChangeInventoryTypeItem(section, builder);
            case "WRITE_TITLE_ITEM" -> new MainInputTextButton(section, builder::title, builder.title());
            case "TO_SELECT_TEMPLATE_ITEM" -> new SwitchItem(section, false,
                    p -> new SelectTemplateMenu(p, builder));
            case "TO_MANAGE_COMMAND_ITEM" -> {
                if (builder.generatorType() != GeneratorType.MENU)
                    yield new Item(section.getConfigurationSection("ifNotAllowed"))
                            .setSlot((byte) section.getInt("slot"));
                else
                    yield new SwitchItem(section, false,
                        p -> new ManageCommandsMenu(p, builder));
            }
            case "TO_MANAGE_ANIMATION_ITEM" -> new SwitchItem(section, false, p ->
                    new ManageAnimationsMenu(p, builder));
            case "EDIT_OPEN_ACTION_ITEM" -> new SwitchItem(section, false,
                    p -> new EditMenuActionMenu(p, builder, builder.openAction(), this));
            case "EDIT_CLOSE_ACTION_ITEM" -> new SwitchItem(section, false,
                    p -> new EditMenuActionMenu(p, builder, builder.closeAction(), this));
            case "EDIT_DECORATIONS" -> new SwitchItem(section, false,
                    p -> new DecorationsMenu(p, builder));
            case "EDIT_ITEMS" -> new SwitchItem(section, false,
                    p -> new ItemsMenu(p, builder));
            case "BUILD_BUTTON" -> new BuildItem(section, builder);
            default -> null;
        };
    }

    @Override
    public boolean back(Player player) {
        return false;
    }

    @Override
    public IMenu reCreate(Player player) {
        return new MainEditMenu(player, builder);
    }

    private class MainInputTextButton extends InputTextButton {
        private final Consumer<String> consumer;
        public MainInputTextButton(ConfigurationSection section,
                                   Consumer<String> consumer,
                                   String nowValue) {
            super(section, MainEditMenu.this.builder, nowValue);
            this.consumer = consumer;
        }

        @Override
        public BiConsumer<InputTextQuery, String> registerAccept() {
            return (q, s) -> {
                consumer.accept(s);
                new MainEditMenu(getPlayer(), builder).open();
            };
        }

        @Override
        public Consumer<InputTextQuery> registerCancel() {
            return q -> new MainEditMenu(getPlayer(), builder).open();
        }
    }
}
