package org.satellite.dev.progiple.satemenus.self.menusCreator.menus;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.novasparkle.lunaspring.API.menus.items.ConsumerItem;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.menus.Backable;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;

import java.util.List;

@Getter
public abstract class AbstractEditMenu extends AMenu implements IEditMenu, Backable {

    protected final MenuSettingsBuilder builder;
    protected final Configuration config;
    public AbstractEditMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player);
        this.builder = builder;

        String menuPath = this.getClass().getAnnotation(MenuId.class).value();
        this.config = new Configuration(SateMenus.getInstance().getDataFolder(), "toolsMenus/" + menuPath);

        String menuName = builder.title() == null ? builder.id() : builder.title();
        this.initialize(
                config.getString("title").replace("[name]", menuName),
                (byte) (config.getInt("rows") * 9),
                config.getSection("decorations"),
                true);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        super.onClick(e);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        ConfigurationSection backSection = config.getSection("items.BACK_BUTTON");
        if (backSection != null) {
            this.addItems(false, new ConsumerItem(backSection, false, (p, e) -> {
                e.setCancelled(true);
                this.back(p);
            }));
        }

        ConfigurationSection itemsSection = config.getSection("items");
        if (itemsSection != null) {
            itemsSection.getKeys(false).forEach(key -> {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    Item item = initializeItem(itemSection);
                    if (item != null)
                        this.addItems(false, item);
                }
            });
        }

        insertAll();
    }

    @Override
    public List<Item> findItems(Class<?> clazz) {
        return getItemList().stream().filter(i -> clazz.isAssignableFrom(i.getClass())).toList();
    }

    @Override
    public boolean onCloseFirstUnregisterFlag() {
        return true;
    }

    public abstract Item initializeItem(ConfigurationSection section);
}
