package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.LoreUpdatable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.MenuId;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.MainEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.OnlySelectMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.selectMenus.items.SelectItem;

@MenuId("selectTemplate")
public class SelectTemplateMenu extends OnlySelectMenu<Template> {
    public SelectTemplateMenu(@NotNull Player player,
                              MenuSettingsBuilder builder) {
        super(player, builder, Templates.getTemplates());
    }

    @Override
    protected SelectItem<Template> createSelectItem(Template value, byte slot) {
        var section = value.equals(builder.template()) ?
                selectItemSection.getConfigurationSection("selected") :
                selectItemSection;
        var item = new SelectTemplateItem(section, builder, value);
        item.setSlot((byte) selectItemSection.getInt("slot"));
        return item;
    }

    @Override
    public boolean back(Player player) {
        new MainEditMenu(player, builder).open();
        return true;
    }

    private static class SelectTemplateItem extends SelectItem<Template> implements LoreUpdatable {
        private final Template template;
        public SelectTemplateItem(ConfigurationSection section,
                                  MenuSettingsBuilder builder,
                                  Template template) {
            super(section, builder);
            this.template = template;
            updateLore();
        }

        @Override
        protected void complete(InventoryClickEvent e) {
            if (template.equals(builder.template()))
                builder.template(null);
            else
                builder.template(template);

            MainEditMenu mainEditMenu = new MainEditMenu((Player) e.getWhoClicked(), builder);
            mainEditMenu.open();
        }

        @Override
        public void updateLore() {
            this.replaceLore(l -> Utils.applyReplacements(l, "id-%-" + template.id()));
        }
    }
}
