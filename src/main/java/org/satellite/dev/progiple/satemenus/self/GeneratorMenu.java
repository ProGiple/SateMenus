package org.satellite.dev.progiple.satemenus.self;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.novasparkle.lunaspring.API.menus.items.Decoration;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuConfiguredItem;

import java.util.List;

@Getter
public class GeneratorMenu extends AMenu implements IGeneratorMenu {
    private final Configuration config;

    @Builder
    public GeneratorMenu(@NotNull Player player,
                         InventoryType invType,
                         String id,
                         GeneratorType type) {
        super(player, id, invType);
        this.config = new Configuration(SateMenus.getInstance().getDataFolder(), type.path + id);
    }

    @Builder
    public GeneratorMenu(@NotNull Player player,
                         int rows,
                         String id,
                         GeneratorType type) {
        super(player, id, (byte) (rows * 9));
        this.config = new Configuration(SateMenus.getInstance().getDataFolder(), type.path + id);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        ConfigurationSection section = config.getSection("decorations");
        if (section != null) {
            new Decoration(section, this.getInventory()).insert(this.getInventory());
        }

        section = config.getSection("items");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                List<Integer> slots = Utils.getSlotList(itemSection.getStringList("slots"));
                if (slots.isEmpty()) slots.add(itemSection.getInt("slot"));

                MenuConfiguredItem configuredItem = MenuConfiguredItem.convert(itemSection);
                for (int slot : slots) {
                    this.addItems(false, new EditableItem(configuredItem, slot));
                }
            }
        }

        this.insertAll();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack != null) {
            Item item = findFirstItem(itemStack, e.getRawSlot());
            if (item != null) {

            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = this.getInventory();
        Bukkit.getScheduler().runTaskAsynchronously(SateMenus.getInstance(), () -> {
            this.saveToConfig(inventory);
        });
    }

    @Override
    public ConfigurationSection sectionFormating(ConfigurationSection section) {
        return IGeneratorMenu.super.sectionFormating(section);
    }
}
