package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.exceptions.SlotIsNotPositiveException;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.LoreUpdatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.MainEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.OnlySelectMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;

@MenuId("manageAnimations")
public class ManageAnimationsMenu extends OnlySelectMenu<String> {
    public ManageAnimationsMenu(@NotNull Player player,
                                MenuSettingsBuilder builder) {
        super(player, builder, Animations.keySet());
    }

    @Override
    protected SelectItem<String> createSelectItem(String value, byte slot) {
        return new SelectAnimationItem(selectItemSection, builder, value);
    }

    @Override
    public boolean back(Player player) {
        new MainEditMenu(player, builder).open();
        return true;
    }

    private static class SelectAnimationItem extends SelectItem<String> implements LoreUpdatable {
        private final String animation;
        private final ConfigurationSection section;
        public SelectAnimationItem(ConfigurationSection section,
                                   MenuSettingsBuilder builder,
                                   String animation) {
            super(section, builder);
            this.animation = animation;
            this.section = section;
            if (builder.animations().contains(animation)) setAll(section.getConfigurationSection("ifEnabled"));
            updateLore();
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            boolean state = builder.animations().contains(animation);
            if (state) {
                builder.animations().remove(animation);
                this.setAll(section);
            }
            else {
                builder.animations().add(animation);
                this.setAll(section.getConfigurationSection("ifEnabled"));
            }

            updateLore();
            this.insert();
        }


        @Override
        public void updateLore() {
            this.replaceLore(l -> Utils.applyReplacements(l, "id-%-" + animation));
        }
    }
}
