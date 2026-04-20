package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.LoreUpdatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.MainEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.SelectAndAppendMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.AdvancedSelectItem;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.ArrayList;

@MenuId("manageCommands")
public class ManageCommandsMenu extends SelectAndAppendMenu<String> {
    public ManageCommandsMenu(@NotNull Player player,
                              MenuSettingsBuilder builder) {
        super(player,
                builder,
                new ArrayList<>(builder.commands().keySet()));
    }

    @Override
    protected SelectItem<String> createAppendedItem(InventoryClickEvent e) {
        String commandId = builder.commands().isEmpty() ? builder.id() : "ExampleCommand-" + Utils.getRKey((byte) 8);
        return new CommandItem(builder, commandId);
    }

    @Override
    protected SelectItem<String> createSelectItem(String value, byte slot) {
        return new CommandItem(builder, value);
    }

    @Override
    public boolean back(Player player) {
        new MainEditMenu(player, builder).open();
        return true;
    }

    public class CommandItem extends AdvancedSelectItem<String> implements LoreUpdatable {
        public CommandItem(MenuSettingsBuilder builder, String command) {
            super(selectItemSection, builder, command);
            if (!builder.commands().getOrDefault(this.value, false))
                setAll(selectItemSection.getConfigurationSection("disabledStatus"));
            updateLore();
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            if (e.getClick().isRightClick()) {
                Player player = (Player) e.getWhoClicked();
                InputTextQuery query = new InputTextQuery(player, builder)
                        .thenAccept((q, s) -> {
                            boolean state = builder.commands().getOrDefault(value, false);
                            builder.commands().remove(value);
                            builder.commands().put(s, state);

                            new ManageCommandsMenu(player, builder).open();
                        })
                        .thenCancel(q -> {
                            new ManageCommandsMenu(player, builder).open();
                        });

                sendEditMessage(selectItemSection, player);
                query.register(player.getUniqueId());
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
            else if (e.getClick().isLeftClick()) {
                boolean value = !builder.commands().getOrDefault(this.value, false);
                builder.commands().put(this.value, value);
                if (value)
                    setAll(selectItemSection);
                else
                    setAll(selectItemSection.getConfigurationSection("disabledStatus"));

                updateLore();
                this.insert();
            }
            else {
                super.onClick(e);
            }
        }

        @Override
        public void updateLore() {
            this.replaceLore(l -> Utils.applyReplacements(l, "command-%-" + value));
        }

        @Override
        public void onAppend(SelectAndAppendMenu<String> menu) {
            builder.commands().put(value, false);
            super.onAppend(menu);
        }

        @Override
        public void onRemove(SelectAndAppendMenu<String> menu) {
            builder.commands().remove(value);
            super.onRemove(menu);
        }
    }
}
